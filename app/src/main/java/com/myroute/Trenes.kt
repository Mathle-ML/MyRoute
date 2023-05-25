package com.myroute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Trenes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trenes)

        val btnCamiones: ImageButton = findViewById(R.id.btnCamiones)
        btnCamiones.setOnClickListener{
            startActivity(Intent(this, Camiones::class.java))
        }
        val btnMap:ImageButton = findViewById(R.id.btnMap)
        btnMap.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}