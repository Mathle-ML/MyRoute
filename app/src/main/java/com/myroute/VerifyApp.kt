package com.myroute

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.myroute.dbmanager.DBCheckUpdates
import org.osmdroid.config.Configuration
import java.time.Duration
import java.time.LocalDateTime


enum class VerificationError {
    PERMISSION_DENIED,
    DB_UPDATE_FAILED
}

class VerifyApp(private val context: MainActivity) {
    private var verificationErrors: MutableList<VerificationError> = mutableListOf()

    private lateinit var dbupdate: DBCheckUpdates

    init {
        verify()
    }

    private fun verify() {
        getPermissions()
        configureOSMDroid()
    }

    private fun configureOSMDroid(){
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
    }

    private fun checkVerificationComplete() {
        if (verificationErrors.isEmpty()) {
            onVerificationComplete()
        } else {
            onVerificationFailed(verificationErrors)
        }
    }

    private fun handleVerificationFailure(error: VerificationError) {
        verificationErrors.add(error)
        checkVerificationComplete()
    }

    private fun getPermissions() {
        val permissions = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val requestCode = 1

        if (isPermissionGranted(permissions)) {
            checkDBUpdate()
        } else {
            ActivityCompat.requestPermissions(context, permissions, requestCode)
        }
    }

    private fun isPermissionGranted(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun checkDBUpdate() {
        dbupdate = DBCheckUpdates(context)
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

        while (!::dbupdate.isInitialized || !dbupdate.isUpdated) {
            val currentTime = LocalDateTime.now()

            if (elapsedTime != Duration.between(startTime, currentTime).seconds) {
                Log.i("MyRoute:DBManager/Update", "Tiempo transcurrido: $elapsedTime")
                Log.i("MyRoute:Infor", "inicializada? ${::dbupdate.isInitialized}")
                if (::dbupdate.isInitialized) {
                    Log.i("MyRoute:Info", "actualizada? ${dbupdate.isUpdated}")
                }
                elapsedTime = Duration.between(startTime, currentTime).seconds
            }

            if (currentTime >= endTime) {
                Log.e("MyRoute:DBManager/Update", "Se tardó demasiado en actualizar la base de datos")
                break
            }
        }

        if (dbupdate.isUpdated) {
            checkVerificationComplete()
        } else {
            handleVerificationFailure(VerificationError.DB_UPDATE_FAILED)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("MyRoute:Info", "Permisos concedidos")
                checkDBUpdate()
            } else {
                handleVerificationFailure(VerificationError.PERMISSION_DENIED)
            }
        }
    }

    private fun onVerificationComplete() {
        context.startApp()
    }

    private fun onVerificationFailed(errors: List<VerificationError>) {
        context.verificationFailed(errors)
    }
}