package com.myroute.dbmanager

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import android.util.Log
import com.myroute.MainActivity
import com.myroute.models.Colonia
import com.myroute.models.Ruta
import org.osmdroid.util.GeoPoint
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class DBManager(private val context: MainActivity) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    //Informacion de la base de datos
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "db.sqlite3"

        private const val TABLE_NAME_RUTAS = "rutas"

        private const val COLUMN_ID_ROUTE = "id_route"
        private const val COLUMN_REF_POINTS = "ref_points"
        private const val COLUMN_REF_STOPS = "ref_stops"
        private const val COLUMN_COLOR = "color"
        private const val COLUMN_TYPE = "type"
    }

    //--------Metodos para el manejo de la base de datos--------//
    override fun onCreate(db: SQLiteDatabase?) {
        val createRutasTable = "CREATE TABLE $TABLE_NAME_RUTAS ($COLUMN_ID_ROUTE TEXT PRIMARY KEY, $COLUMN_REF_POINTS TEXT, $COLUMN_REF_STOPS TEXT, $COLUMN_COLOR TEXT, $COLUMN_TYPE TEXT)"
        db?.execSQL(createRutasTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_RUTAS")
        onCreate(db)
    }
    //----------------------------------------------------------//

    //-------------Metodos para obtener los modelos-------------//
    fun addRoute(idRoute: String, refPoints: ArrayList<DoubleArray>, refStops: ArrayList<DoubleArray>, color: String, type: String) {
        val db = this.writableDatabase

        // Checamos si la ruta existe
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME_RUTAS WHERE $COLUMN_ID_ROUTE=?", arrayOf(idRoute))
        if (cursor.count == 0) {
            // Si no existe insertamos la ruta en la tabla
            val values = ContentValues()
            values.put(COLUMN_ID_ROUTE, idRoute)

            // Convertimos los puntos de referencia a string
            val refPointsStr = refPoints.joinToString("|") { point -> "${point[0]},${point[1]}" }
            values.put(COLUMN_REF_POINTS, refPointsStr)

            // Convertimos los puntos de parada a string
            val refStopsStr = refStops.joinToString("|") { point -> "${point[0]},${point[1]}" }
            values.put(COLUMN_REF_STOPS, refStopsStr)

            values.put(COLUMN_COLOR, color)
            values.put(COLUMN_TYPE, type)

            db.insert(TABLE_NAME_RUTAS, null, values)
        } else {
            // Si existe solo actualizamos la ruta
            val values = ContentValues()

            // Convertimos los puntos de referencia a string
            val refPointsStr = refPoints.joinToString("|") { point -> "${point[0]},${point[1]}" }
            values.put(COLUMN_REF_POINTS, refPointsStr)

            // Convertimos los puntos de parada a string
            val refStopsStr = refStops.joinToString("|") { point -> "${point[0]},${point[1]}" }
            values.put(COLUMN_REF_STOPS, refStopsStr)

            values.put(COLUMN_COLOR, color)
            values.put(COLUMN_TYPE, type)

            db.update(TABLE_NAME_RUTAS, values, "$COLUMN_ID_ROUTE=?", arrayOf(idRoute))
        }

        cursor.close()
        db.close()
    }
    @SuppressLint("Range")
    fun getRoute(idRoute: String): Ruta? {


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
        val color = cursor.getInt(cursor.getColumnIndex(COLUMN_COLOR))

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

    //----------------------------------------------------------//
    fun copyDatabaseToDocumentsDirectory(): Boolean {
        val databasePath = context.getDatabasePath(DATABASE_NAME).absolutePath

        val filesDir = context.filesDir
        val databaseDir = File(filesDir, "basededatos")
        val destinationDirectoryPath: String = databaseDir.absolutePath

        if (!databaseDir.exists()) {
            // Si el directorio de base de datos no existe, crea el directorio
            if (!databaseDir.mkdirs()) {
                // No se pudo crear el directorio de base de datos
                return false
            }
        }

        val destinationFilePath = File(databaseDir, "db.sqlite3")

        return try {
            val sourceFile = File(databasePath)
            if (!sourceFile.exists() || !sourceFile.isFile) {
                // La base de datos de origen no existe o no es un archivo válido
                return false
            }

            val inputStream = FileInputStream(sourceFile)
            val outputStream = FileOutputStream(destinationFilePath)

            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}