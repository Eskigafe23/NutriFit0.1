package com.example.nutrifit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPlan = findViewById<Button>(R.id.btnPlan)
        val btnTips = findViewById<Button>(R.id.btnTips)
        val btnCalorias = findViewById<Button>(R.id.btnCalorias)

        btnPlan.setOnClickListener {
            startActivity(Intent(this, PlanActivity::class.java))
        }

        btnTips.setOnClickListener {
            startActivity(Intent(this, TipsActivity::class.java))
        }

        btnCalorias.setOnClickListener {
            startActivity(Intent(this, CaloriasActivity::class.java))
        }
    }
}