package com.myroute

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    private lateinit var verifyResult: VerifyApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        mainContext = this
        verifyResult = VerifyApp(this)
    }

    fun startApp() {
        initializeViews()
        initializeEvents()
    }

    private fun initializeViews() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val bottomBar: CardView = findViewById(R.id.cardView)
        val btnCamiones: ImageButton = findViewById(R.id.btnCamiones)
        val btnTrenes: ImageButton = findViewById(R.id.btnTrenes)
        val btnMap: ImageButton = findViewById(R.id.btnMap)
        val btnMenu: ImageView = findViewById(R.id.menu)

        // Asignar vistas a las propiedades estáticas
        Companion.toolbar = toolbar
        Companion.bottomBar = bottomBar
        Companion.btnCamiones = btnCamiones
        Companion.btnTrenes = btnTrenes
        Companion.btnMap = btnMap
        Companion.btnMenu = btnMenu
    }

    private fun initializeEvents() {
        btnMenu.setOnClickListener {
            when {
                isFragmentAbout -> {findNavController(R.id.mainContainer).navigate(R.id.action_fragmentAbout_to_fragmentMenu);isFragmentAbout = false}
                isFragmentTerms -> {findNavController(R.id.mainContainer).navigate(R.id.action_fragmentTerms_to_fragmentMenu);isFragmentTerms = false}
                isFragmentPricavity -> {findNavController(R.id.mainContainer).navigate(R.id.action_fragmentPrivacity_to_fragmentMenu);isFragmentPricavity = false}
                isFragmentMap -> findNavController(R.id.mainContainer).navigate(R.id.action_fragmentMap_to_fragmentMenu)
                isFragmentCamiones -> findNavController(R.id.mainContainer).navigate(R.id.action_fragmentCamiones_to_fragmentMenu)
                isFragmentTrenes -> findNavController(R.id.mainContainer).navigate(R.id.action_fragmentTrenes_to_fragmentMenu)
            }
        }

        btnMap.isSelected = true
        btnMap.setOnClickListener {
            when {
                isFragmentCamiones -> {
                    findNavController(R.id.mainContainer).navigate(R.id.action_fragmentCamiones_to_fragmentMap)
                    isFragmentMap = true
                    isFragmentCamiones = false
                    btnCamiones.isSelected = false
                }
                isFragmentTrenes -> {
                    findNavController(R.id.mainContainer).navigate(R.id.action_fragmentTrenes_to_fragmentMap)
                    isFragmentMap = true
                    isFragmentTrenes = false
                    btnTrenes.isSelected = false
                }
            }
            btnMap.isSelected = true
        }

        btnCamiones.setOnClickListener {
            when {
                isFragmentMap -> {
                    findNavController(R.id.mainContainer).navigate(R.id.action_fragmentMap_to_fragmentCamiones)
                    isFragmentCamiones = true
                    isFragmentMap = false
                    btnMap.isSelected = false
                }
                isFragmentTrenes -> {
                    findNavController(R.id.mainContainer).navigate(R.id.action_fragmentTrenes_to_fragmentCamiones)
                    isFragmentCamiones = true
                    isFragmentTrenes = false
                    btnTrenes.isSelected = false
                }
            }
            btnCamiones.isSelected = true
        }

        btnTrenes.setOnClickListener {
            when {
                isFragmentMap -> {
                    findNavController(R.id.mainContainer).navigate(R.id.action_fragmentMap_to_fragmentTrenes)
                    isFragmentTrenes = true
                    isFragmentMap = false
                    btnMap.isSelected = false
                }
                isFragmentCamiones -> {
                    findNavController(R.id.mainContainer).navigate(R.id.action_fragmentCamiones_to_fragmentTrenes)
                    isFragmentTrenes = true
                    isFragmentCamiones = false
                    btnCamiones.isSelected = false
                }
            }
            btnTrenes.isSelected = true
        }
    }

    fun verificationFailed(errors: List<VerificationError>) {
        // Mostrar vista de error
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        verifyResult.onRequestPermissionsResult(requestCode, grantResults)
    }

    companion object {
        var isFragmentMap = true
        var isFragmentCamiones = false
        var isFragmentTrenes = false
        var isFragmentAbout = false
        var isFragmentTerms = false
        var isFragmentPricavity = false

        @SuppressLint("StaticFieldLeak")
        lateinit var mainContext: MainActivity

        @SuppressLint("StaticFieldLeak")
        lateinit var toolbar: Toolbar
        @SuppressLint("StaticFieldLeak")
        lateinit var bottomBar: CardView
        @SuppressLint("StaticFieldLeak")
        lateinit var btnCamiones: ImageButton
        @SuppressLint("StaticFieldLeak")
        lateinit var btnTrenes: ImageButton
        @SuppressLint("StaticFieldLeak")
        lateinit var btnMap: ImageButton
        @SuppressLint("StaticFieldLeak")
        lateinit var btnMenu: ImageView
    }
}