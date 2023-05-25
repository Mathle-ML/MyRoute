package com.myroute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Camiones : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camiones)

        val btnMap:ImageButton = findViewById(R.id.btnMap)
        btnMap.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
        val btnTrenes: ImageButton = findViewById(R.id.btnTrenes)
        btnTrenes.setOnClickListener{
            startActivity(Intent(this, Trenes::class.java))
        }
    }
}