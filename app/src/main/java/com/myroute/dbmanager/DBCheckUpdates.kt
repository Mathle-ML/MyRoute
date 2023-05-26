package com.myroute.dbmanager

import android.os.Environment
import android.util.Log
import com.myroute.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

class DBCheckUpdates(val context: MainActivity) {
    // Variable para verificar si el proceso (corrutina) de verificacion de actualizaciones a finalizado
    var isCFUCompleted: Boolean = false

    // Variable para verificar si la base de datos ha sido o esta actualizada
    var isUpdated: Boolean = false



    init {
        // Creacion de corrutina para la verificacion de actualizaciones de la base de datos
        GlobalScope.launch (Dispatchers.IO) {
            // Muestra que ya hay un proceso de actualizacion (para evitar que se )


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
    fun copyDatabaseToDocumentsDirectory(): Boolean {
        val databasePath = context.getDatabasePath("db.sqlite3").absolutePath
        val destinationDirectoryPath = Environment.getExternalStorageDirectory().absolutePath + "/documentos"
        val sourceFile = File(databasePath)
        val destinationDirectory = File(destinationDirectoryPath)

        if (!sourceFile.exists() || !sourceFile.isFile) {
            // La base de datos de origen no existe o no es un archivo válido
            return false
        }

        if (!destinationDirectory.exists()) {
            // Si el directorio de destino no existe, crea el directorio
            if (!destinationDirectory.mkdirs()) {
                // No se pudo crear el directorio de destino
                return false
            }
        }

        val destinationFilePath = File(destinationDirectory, sourceFile.name)

        return try {
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