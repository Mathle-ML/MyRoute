package com.myroute

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.myroute.mapmanager.MapManager
import kotlinx.coroutines.Deferred

class MainActivity : AppCompatActivity() {
    var coroutineManager: Deferred<Unit>? = null
    private lateinit var verifyResult: VerifyApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verifyResult = VerifyApp(this)
    }

    fun startApp(){
        val mapManager = MapManager(this)
        mapManager.generateMap()
        mapManager.generateRoute("C47_Kilometro 13 | Kilometro 13")
    }

    fun verificationFailed(errors: List<VerificationError>){

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        verifyResult.onRequestPermissionsResult(requestCode, grantResults)
    }
}