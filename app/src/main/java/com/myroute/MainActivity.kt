package com.myroute

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.myroute.dbmanager.DBCheckUpdates
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
        getSupportActionBar()?.hide()
        val mapManager = MapManager(this)
        mapManager.generateMap()
        mapManager.generateRoute("C47_Kilometro 13 | Kilometro 13")
        
        var IsFragmentMap = true
        var IsFragmentCamiones = false
        var IsFragmentTrenes = false

        val btnCamiones: ImageButton = findViewById(R.id.btnCamiones)
        val btnTrenes: ImageButton = findViewById(R.id.btnTrenes)
        val btnMap: ImageButton = findViewById(R.id.btnMap)
        btnMap.setOnClickListener{
            if(IsFragmentCamiones){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentMap)
                IsFragmentMap = true
                IsFragmentCamiones = false
            }else if(IsFragmentTrenes){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentTrenes_to_fragmentMap)
                IsFragmentMap = true
                IsFragmentTrenes = false
            }
        }
        btnCamiones.setOnClickListener{
            if(IsFragmentMap){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentMap_to_fragmentCamiones)
                IsFragmentCamiones = true
                IsFragmentMap = false
            }else if(IsFragmentTrenes){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentTrenes_to_fragmentCamiones)
                IsFragmentCamiones = true
                IsFragmentTrenes = false
            }
        }
        btnTrenes.setOnClickListener{
            if(IsFragmentMap){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentMap_to_fragmentTrenes)
                IsFragmentTrenes = true
                IsFragmentMap = false
            }else if(IsFragmentCamiones){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentTrenes)
                IsFragmentTrenes = true
                IsFragmentCamiones = false
            }
        }
    }

    fun verificationFailed(errors: List<VerificationError>){

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        verifyResult.onRequestPermissionsResult(requestCode, grantResults)
    }
}
