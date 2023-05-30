package com.myroute

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.myroute.dbmanager.DBCheckUpdates
import com.myroute.fragments.FragmentMap
import org.osmdroid.config.Configuration
import java.time.Duration
import java.time.LocalDateTime

class VerifyApp(private val context: MainActivity) {
    private lateinit var dbUpdate: DBCheckUpdates

    init {
        verify()
    }

    private fun verify() {
        if (!configureOSMDroid()) {
            context.showErrorDialog("Error en OSMDroid",
                "La aplicación ha experimentado un error al intentar configurar OSMDroid. " +
                        "Le sugerimos que intente abrir la aplicación nuevamente para resolver este problema. " +
                        "Si el error persiste, le recomendamos ponerse en contacto con nuestro equipo de soporte para obtener ayuda adicional.")
        } else {
            getPermissions()
            checkDBUpdate()
        }
    }

    private fun configureOSMDroid(): Boolean {
        return try {
            Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun getPermissions() {
        val permissions = arrayOf(
            Pair(android.Manifest.permission.READ_EXTERNAL_STORAGE, 1),
            Pair(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, 1),
            Pair(android.Manifest.permission.INTERNET, 2),
            Pair(android.Manifest.permission.ACCESS_COARSE_LOCATION, 3),
            Pair(android.Manifest.permission.ACCESS_FINE_LOCATION, 3)
        )

        val permissionsToRequest = permissions.filter { !isPermissionGranted(it.first) }

        if (permissionsToRequest.isNotEmpty()) {
            val requestCode = permissionsToRequest.first().second
            val permissionArray = permissionsToRequest.map { it.first }.toTypedArray()
            ActivityCompat.requestPermissions(context, permissionArray, requestCode)
        }else{
            FragmentMap.hasLocationPermission = true
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkDBUpdate() {
        dbUpdate = DBCheckUpdates(context)
        waitForUpdates()
    }

    private fun waitForUpdates() {
        val startTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        var elapsedTime = 0L
        val endTime = startTime.plusSeconds(60)

        while (!::dbUpdate.isInitialized || !dbUpdate.isUpdated) {
            val currentTime = LocalDateTime.now()

            if (elapsedTime != Duration.between(startTime, currentTime).seconds) {
                Log.i("MyRoute:DBManager/Update", "Tiempo transcurrido: $elapsedTime")
                elapsedTime = Duration.between(startTime, currentTime).seconds
            }

            if (currentTime >= endTime) {
                Log.e("MyRoute:DBManager/Update", "Se tardó demasiado en actualizar la base de datos")
                break
            }
        }

        if (!dbUpdate.isUpdated) {
            context.showWarnDialog("Base de datos desactualizada",
                "La base de datos no pudo actualizarse y puede contener información de rutas desactualizada. " +
                        "Por favor, intente abrir la aplicación nuevamente para solucionar este error. " +
                        "Si el problema persiste, le recomendamos ponerse en contacto con nuestro equipo de soporte para obtener asistencia adicional.")
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    context.showErrorDialog("Permisos denegados",
                        "Para ejecutar la aplicación se necesitan permisos de escritura y lectura que no se han concedido.")
                }
            }
            2 -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    context.showErrorDialog("Permisos denegados",
                        "Para actualizar la base de datos se necesitan permisos de acceso a internet que no se han concedido.")
                }
            }
            3 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FragmentMap.hasLocationPermission = true
                }else{
                    context.showWarnDialog("Permisos denegados",
                        "La aplicación requiere el permiso de ubicación para mostrar su posición en el mapa. " +
                                "Actualmente, el permiso de ubicación no ha sido concedido, lo que puede afectar negativamente " +
                                "la experiencia de uso de la aplicación y la capacidad de ver su ubicación en el mapa.")
                }
            }
        }
    }
}