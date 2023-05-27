package com.myroute

import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.myroute.fragments.FragmentMap
import kotlinx.coroutines.Deferred

class MainActivity : AppCompatActivity() {
    var coroutineManager: Deferred<Unit>? = null
    private lateinit var verifyResult: VerifyApp

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FragmentMap.maincontext = this
        supportActionBar?.hide()

        setSupportActionBar(findViewById(R.id.toolbar))

        verifyResult = VerifyApp(this)
    }

    fun startApp(){
        var isFragmentMap = true
        var isFragmentCamiones = false
        var isFragmentTrenes = false

        val btnCamiones: ImageButton = findViewById(R.id.btnCamiones)
        val btnTrenes: ImageButton = findViewById(R.id.btnTrenes)
        val btnMap: ImageButton = findViewById(R.id.btnMap)
        btnMap.setSelected(true)
        btnMap.setOnClickListener{

            if(isFragmentCamiones){
                FragmentMap.id_route = "C47_Kilometro 13 | Kilometro 13"
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentMap)
                isFragmentMap = true
                isFragmentCamiones = false
                btnCamiones.setSelected(false)
            }else if(isFragmentTrenes){
                FragmentMap.id_route = "C47-Panteon | Dos templos"
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentTrenes_to_fragmentMap)
                isFragmentMap = true
                isFragmentTrenes = false
                btnTrenes.setSelected(false)
            }
            btnMap.setSelected(true)

        }
        btnCamiones.setOnClickListener{
            if(isFragmentMap){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentMap_to_fragmentCamiones)
                isFragmentCamiones = true
                isFragmentMap = false
                btnMap.setSelected(false)
            }else if(isFragmentTrenes){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentTrenes_to_fragmentCamiones)
                isFragmentCamiones = true
                isFragmentTrenes = false
                btnTrenes.setSelected(false)
            }
            btnCamiones.setSelected(true)
        }
        btnTrenes.setOnClickListener{
            if(isFragmentMap){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentMap_to_fragmentTrenes)
                isFragmentTrenes = true
                isFragmentMap = false
                btnMap.setSelected(false)
            }else if(isFragmentCamiones){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentTrenes)
                isFragmentTrenes = true
                isFragmentCamiones = false
                btnCamiones.setSelected(false)
            }
            btnTrenes.setSelected(true)
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
