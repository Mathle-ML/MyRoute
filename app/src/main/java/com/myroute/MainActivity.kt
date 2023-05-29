package com.myroute

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.myroute.fragments.FragmentCamiones
import com.myroute.fragments.FragmentMap
import kotlinx.coroutines.Deferred

class MainActivity : AppCompatActivity() {
    companion object{
        var isFragmentMap = true
        var isFragmentCamiones = false
        var isFragmentTrenes = false
        lateinit var btnCamiones: ImageButton
        lateinit var btnTrenes: ImageButton
        lateinit var btnMap: ImageButton
        lateinit var mainContext : MainActivity
        lateinit var toolbar: Toolbar
        lateinit var bottomBar : CardView
    }

    private lateinit var verifyResult: VerifyApp

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*supportActionBar?.hide()
        setSupportActionBar(findViewById(R.id.toolbar))*/
        setSupportActionBar(findViewById(R.id.toolbar))

        mainContext = this

        verifyResult = VerifyApp(this)
    }

    fun startApp(){

        instantiateEvents()


    }

    fun instantiateEvents(){

        toolbar = findViewById(R.id.toolbar)
        bottomBar = findViewById(R.id.cardView)
        btnCamiones = findViewById(R.id.btnCamiones)
        btnTrenes = findViewById(R.id.btnTrenes)
        btnMap = findViewById(R.id.btnMap)

        val btnMenu = findViewById<ImageView>(R.id.menu)

        btnMenu.setOnClickListener {
            if(isFragmentMap){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentMap_to_fragmentMenu)
            }else if(isFragmentCamiones){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentMenu)
            }else if (isFragmentTrenes){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentTrenes_to_fragmentMenu)
            }
        }

        btnMap.setSelected(true)
        btnMap.setOnClickListener{
            if(isFragmentCamiones){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentMap)
                isFragmentMap = true
                isFragmentCamiones = false
                btnCamiones.setSelected(false)
            }else if(isFragmentTrenes){
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
