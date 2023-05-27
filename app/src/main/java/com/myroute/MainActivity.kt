package com.myroute

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.myroute.fragments.FragmentMap
import kotlinx.coroutines.Deferred

class MainActivity : AppCompatActivity() {
    var coroutineManager: Deferred<Unit>? = null
    private lateinit var verifyResult: VerifyApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        verifyResult = VerifyApp(this)
    }

    fun startApp(){
        FragmentMap.generateRoute(this,"C47_Kilometro 13 | Kilometro 13")
        var isFragmentMap = true
        var isFragmentCamiones = false
        var isFragmentTrenes = false

        val btnCamiones: ImageButton = findViewById(R.id.btnCamiones)
        val btnTrenes: ImageButton = findViewById(R.id.btnTrenes)
        val btnMap: ImageButton = findViewById(R.id.btnMap)
        btnMap.setOnClickListener{
            if(isFragmentCamiones){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentMap)
                isFragmentMap = true
                isFragmentCamiones = false
            }else if(isFragmentTrenes){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentTrenes_to_fragmentMap)
                isFragmentMap = true
                isFragmentTrenes = false
            }
        }
        btnCamiones.setOnClickListener{
            if(isFragmentMap){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentMap_to_fragmentCamiones)
                isFragmentCamiones = true
                isFragmentMap = false
            }else if(isFragmentTrenes){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentTrenes_to_fragmentCamiones)
                isFragmentCamiones = true
                isFragmentTrenes = false
            }
        }
        btnTrenes.setOnClickListener{
            if(isFragmentMap){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentMap_to_fragmentTrenes)
                isFragmentTrenes = true
                isFragmentMap = false
            }else if(isFragmentCamiones){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentTrenes)
                isFragmentTrenes = true
                isFragmentCamiones = false
            }
        }
    }

    fun verificationFailed(errors: List<VerificationError>){
        //Mostrar view de error
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        verifyResult.onRequestPermissionsResult(requestCode, grantResults)
    }
}
