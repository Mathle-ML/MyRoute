package com.myroute

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController


class MainActivity : AppCompatActivity() {

    private lateinit var verifyResult: VerifyApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.startApp()

        mainContext = this
        verifyResult = VerifyApp(this)

    }

    fun startApp() {
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        checkTermsAndConditions()

        initializeViews()
        initializeEvents()
    }

    fun checkTermsAndConditions(){
        if(!TermsManager.areTermsAccepted(this))showTermsDialog()
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

    fun showErrorDialog(errorCode : String, errorDesc : String){
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.error_layout)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dialog.setOnCancelListener{
            finish()
        }

        val errorCodeView = dialog.findViewById<TextView>(R.id.errorCode)
        val errorDescView = dialog.findViewById<TextView>(R.id.errorDesc)
        errorCodeView.text = errorCode
        errorDescView.text = errorDesc
    }

    fun showWarnDialog(warnCode : String, warnDesc : String){
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.warn_layout)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val warnCodeView = dialog.findViewById<TextView>(R.id.warnCode)
        val warnDescView = dialog.findViewById<TextView>(R.id.warnDesc)
        warnCodeView.text = warnCode
        warnDescView.text = warnDesc
    }

    fun showTermsDialog(){
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.show_terms)
        dialog.setCancelable(false)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        val btnAceptar = dialog.findViewById<Button>(R.id.btnAceptar)
        val btnCancelar = dialog.findViewById<Button>(R.id.btnCancelar)
        val btnTextTerms = dialog.findViewById<TextView>(R.id.tvTerms)
        val btnTexTPriv = dialog.findViewById<TextView>(R.id.tvPriv)

        btnTextTerms.setOnClickListener{
            dialog.cancel()
            when {
                isFragmentMap -> findNavController(R.id.mainContainer).navigate(R.id.action_fragmentMap_to_fragmentMenu)
                isFragmentCamiones -> findNavController(R.id.mainContainer).navigate(R.id.action_fragmentCamiones_to_fragmentMenu)
                isFragmentTrenes -> findNavController(R.id.mainContainer).navigate(R.id.action_fragmentTrenes_to_fragmentMenu)
            }
            findNavController(R.id.mainContainer).navigate(R.id.action_fragmentMenu_to_fragmentTerms)
        }

        btnTexTPriv.setOnClickListener{
            dialog.cancel()
            when {
                isFragmentMap -> findNavController(R.id.mainContainer).navigate(R.id.action_fragmentMap_to_fragmentMenu)
                isFragmentCamiones -> findNavController(R.id.mainContainer).navigate(R.id.action_fragmentCamiones_to_fragmentMenu)
                isFragmentTrenes -> findNavController(R.id.mainContainer).navigate(R.id.action_fragmentTrenes_to_fragmentMenu)
            }
            findNavController(R.id.mainContainer).navigate(R.id.action_fragmentMenu_to_fragmentPrivacity)
        }

        btnAceptar.setOnClickListener{
            TermsManager.setTermsAccepted(this)
            dialog.cancel()
        }
        btnCancelar.setOnClickListener {
            finish()
        }

        dialog.show()

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