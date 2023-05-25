package com.myroute.dbmanager

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.myroute.MainActivity
import com.myroute.models.Colonia
import com.myroute.models.Ruta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import org.osmdroid.util.GeoPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

class DBManager(val context: MainActivity) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // Variable para verificar si el proceso (corrutina) de verificacion de actualizaciones a finalizado
    private var isCFUCompleted: Boolean = false

    // Variable para verificar si la base de datos ha sido o esta actualizada
    private var isUpdated: Boolean = false

    init {
        // Creacion de corrutina para la verificacion de actualizaciones de la base de datos
        GlobalScope.launch (Dispatchers.IO) {

            // Inicion de proceso de verficacion de actualizaciones de la base de datos
            isUpdated = checkForUpdates()

            if (!isUpdated){
                Log.w("MyRoute:DBManager/Update", "No se pudo completar la verificacion de " +
                        "actualizaciones, puede que este usando una base de datos desactualizada")
            }

            // Se marca como completo el proceso de verificacion de actualzaciones de la base de datos
            isCFUCompleted = true
        }
    }

    //Informacion de la base de datos
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "db.sqlite3"
        private const val TABLE_NAME_RUTAS = "rutas"
        private const val TABLE_NAME_COLONIAS = "colonias"
        private const val COLUMN_ID_ROUTE = "id_route"
        private const val COLUMN_REF_POINTS = "ref_points"
        private const val COLUMN_REF_STOPS = "ref_stops"
        private const val COLUMN_COLOR = "color"
        private const val COLUMN_ID_COLN = "id_coln"
        private const val COLUMN_REF_ROUTS = "ref_routs"
        private const val COLUMN_COLN_RADIO = "coln_radio"
        private const val COLUMN_COLN_COLIN = "coln_colin"
    }

    //--------Metodos para el manejo de la base de datos--------//
    override fun onCreate(db: SQLiteDatabase?) {

        val createRutasTable = "CREATE TABLE $TABLE_NAME_RUTAS ($COLUMN_ID_ROUTE TEXT PRIMARY KEY, $COLUMN_REF_POINTS TEXT, $COLUMN_REF_STOPS TEXT, $COLUMN_COLOR TEXT)"
        val createColoniasTable = "CREATE TABLE $TABLE_NAME_COLONIAS ($COLUMN_ID_COLN TEXT PRIMARY KEY, $COLUMN_REF_ROUTS TEXT, $COLUMN_COLN_RADIO REAL, $COLUMN_COLN_COLIN TEXT)"
        db?.execSQL(createRutasTable)
        db?.execSQL(createColoniasTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_RUTAS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_COLONIAS")
        onCreate(db)
    }
    //----------------------------------------------------------//

    //-------------Metodos para obtener los modelos-------------//

    @SuppressLint("Range")
    fun getRoute(idRoute: String): Ruta? {
        if (!isCFUCompleted)return null

        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT $COLUMN_REF_POINTS, $COLUMN_REF_STOPS, $COLUMN_COLOR FROM $TABLE_NAME_RUTAS WHERE $COLUMN_ID_ROUTE=?", arrayOf(idRoute))

        if (cursor.count == 0) {
            cursor.close()
            db.close()
            return null
        }

        cursor.moveToFirst()

        val refPointsStr = cursor.getString(cursor.getColumnIndex(COLUMN_REF_POINTS))
        val refStopsStr = cursor.getString(cursor.getColumnIndex(COLUMN_REF_STOPS))
        val color = cursor.getString(cursor.getColumnIndex(COLUMN_COLOR))

        cursor.close()
        db.close()
    
        // Convertimos los string a array de GeoPoint
        val mutablepointsList = refPointsStr.split("|").map { pointStr ->
            val latLngStr = pointStr.split(",")
            val lat = latLngStr[0].toDouble()
            val lng = latLngStr[1].toDouble()
            GeoPoint(lat, lng)
        }.toMutableList()

        val mutablestopsList = refStopsStr.split("|").map { pointStr ->
            val latLngStr = pointStr.split(",")
            val lat = latLngStr[0].toDouble()
            val lng = latLngStr[1].toDouble()
            GeoPoint(lat, lng)
        }.toMutableList()

        val refPointsArray: ArrayList<GeoPoint> = ArrayList(mutablepointsList)
        val refStopsArray: ArrayList<GeoPoint> = ArrayList(mutablestopsList)

        return Ruta(idRoute, refPointsArray, refStopsArray, color)
    }

    @SuppressLint("Range")
    fun getAllRoutes(): ArrayList<Ruta>? {
        if (!isCFUCompleted)return null

        val routes = ArrayList<Ruta>()
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT $COLUMN_ID_ROUTE FROM $TABLE_NAME_RUTAS", null)

        if (cursor.moveToFirst()) {
            do {
                val idRoute = cursor.getString(cursor.getColumnIndex(COLUMN_ID_ROUTE))
                val route = getRoute(idRoute) // Utiliza el método existente getRoute para obtener cada ruta individualmente
                route?.let {
                    routes.add(it)
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return routes
    }

    @SuppressLint("Range")
    fun getColonia(idColn: String): Colonia? {
        if (!isCFUCompleted)return null

        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME_COLONIAS WHERE $COLUMN_ID_COLN=?", arrayOf(idColn))
        if (cursor.count == 0) {
            cursor.close()
            db.close()
            return null
        }

        cursor.moveToFirst()

        val refRoutsStr = cursor.getString(cursor.getColumnIndex(COLUMN_REF_ROUTS))
        val colnColinStr = cursor.getString(cursor.getColumnIndex(COLUMN_COLN_COLIN))
        val colnRadio = cursor.getDouble(cursor.getColumnIndex(COLUMN_COLN_RADIO))

        cursor.close()
        db.close()

        val refRoutsArray = refRoutsStr.split(",").toTypedArray()
        val colnColinArray = colnColinStr.split(",").toTypedArray()

        return Colonia(idColn, refRoutsArray, colnColinArray, colnRadio)
    }
    //----------------------------------------------------------//

    //Funciones para verificar actualizaciones para la base de datos//
    private suspend fun checkForUpdates() : Boolean{

        var timeout = 0

        val userVersion = getUserVersion()
        val githubVersion = getGitHubVersion()

        if (githubVersion == null){
            Log.e("MyRoute:DBManager/Update", "La version (string) obtenida de github es nula")
            return false
        }

        if (userVersion != githubVersion){
            updateDatabase()
            while (!isUpdated){
                delay(1000L)
                timeout+=1
                Log.i("MyRoute:DBManager/Update", "Esperando al proceso de actaulizacion, tiempo transcurrido: " + timeout + " segundos")
                if(timeout >= 90){
                    Log.e("MyRoute:DBManager/Update", "El tiempo de espera sobrepaso los 90 segundos")
                    break
                }
            }
            if (isUpdated){
                modifyVersionTextFile(githubVersion)
                Log.i("MyRoute:DBManager/Update", "Se logro actualizar la base de datos con exito")
            }else{
                return false
            }
        }else{
            Log.i("MyRoute:DBManager/Update", "La base de datos esta actualizada, no requiere ningun cambio")
        }

        return true
    }

    private fun getUserVersion() : String?{
        return try {
            val file = File(context.filesDir, "version.txt")

            if (!file.exists()) {
                file.createNewFile()
                return "noversion"
            }

            val inputStream = FileInputStream(file)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: Exception) {
            Log.e("MyRoute:DBManager/Update", "Hubo un error al obtener la versión del usuario: " + e.message)
            null
        }
    }
    private fun getGitHubVersion() : String?{
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://raw.githubusercontent.com/Mathle-ML/MyRoute/database/version.txt")
            .build()

        val response = client.newCall(request).execute()

        return response.body?.string()
    }

    private fun updateDatabase() {
        val databasePath = context.getDatabasePath("db.sqlite3")
        if (!databasePath.exists()) {
            // La base de datos no existe, crearla primero
            val isDatabaseCreated = createDatabase(databasePath)
            if (!isDatabaseCreated) {
                // Muestra en LOG:ERROR diciendo que no se pudo crear la base de datos
                Log.e("MyRoute:DBManager/Update", "No se pudo crear la base de datos en la ubicación deseada")
                return
            }
        }

        val url = "https://raw.githubusercontent.com/Mathle-ML/MyRoute/database/db.sqlite3"
        val client = OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS) // Establecer tiempo límite de espera en segundos
            .build()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Muestra un LOG:ERROR con mensaje de error al no poder conectar
                Log.e("MyRoute:DBManager/Update", "No se pudo conectar con la URL: " + e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val inputStream = response.body?.byteStream()
                    if (inputStream != null) {

                        // Guarda el archivo en el directorio principal de la aplicación
                        val outputFile = File(context.filesDir, "db.sqlite3")
                        val outputStream = FileOutputStream(outputFile)
                        inputStream.copyTo(outputStream)
                        outputStream.close()
                        inputStream.close()

                        // Llamar al método para copiar la base de datos desde el archivo descargado
                        // a la ubicación deseada en la aplicación
                        val externalDbFile = File(outputFile.absolutePath)
                        val internalDbPath = databasePath.absolutePath

                        try {
                            val inputChannel = FileInputStream(externalDbFile).channel
                            val outputChannel = FileOutputStream(internalDbPath).channel

                            outputChannel.transferFrom(inputChannel, 0, inputChannel.size())

                            inputChannel.close()
                            outputChannel.close()

                            // ¡La base de datos ha sido copiada exitosamente!
                            isUpdated = true

                            // Eliminar la base de datos descargada
                            outputFile.delete()
                        } catch (e: IOException) {
                            // Manejar error de copia de la base de datos
                            Log.e("MyRoute:DBManager/Update", "No se pudo copiar la base de datos: " + e.message)
                            outputFile.delete()
                        }
                    } else {
                        // Muestra en LOG:ERROR diciendo que el archivo es nulo
                        Log.e("MyRoute:DBManager/Update", "La base de datos descargada es NULA")
                    }
                } else {
                    // Muestra un LOG:ERROR con mensaje de error al no poder descargar la base de datos
                    Log.e("MyRoute:DBManager/Update", "No se pudo descargar la base de datos: " + response.message)
                }
            }
        })
    }

    private fun createDatabase(databasePath: File): Boolean {
        return try {
            // Crea la base de datos vacía en la ubicación deseada
            val outputChannel = FileOutputStream(databasePath).channel
            outputChannel.close()
            true
        } catch (e: IOException) {
            // Manejar error al crear la base de datos
            Log.e("MyRoute:DBManager/Update", "No se pudo crear la base de datos: " + e.message)
            false
        }
    }

    private fun modifyVersionTextFile(version: String) {
        try {
            val outputFile = File(context.filesDir, "version.txt")
            outputFile.delete()

            val outputStream = FileOutputStream(outputFile)
            outputStream.write(version.toByteArray())
            outputStream.close()

            // El archivo ha sido modificado exitosamente
        } catch (e: IOException) {
            // Manejar error de lectura o escritura del archivo
            Log.e("MyRoute:DBManager/Update", "Error al modificar el archivo: " + e.message)
        }
    }
}