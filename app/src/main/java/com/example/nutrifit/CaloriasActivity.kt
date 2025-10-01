package com.example.nutrifit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CaloriasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calorias)

        val etPeso = findViewById<EditText>(R.id.etPeso)
        val etEjercicio = findViewById<EditText>(R.id.etEjercicio)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        btnCalcular.setOnClickListener {
            val peso = etPeso.text.toString().toDoubleOrNull()
            val minutos = etEjercicio.text.toString().toIntOrNull()

            if (peso != null && minutos != null) {
                // Fórmula super simple: calorías ≈ peso(kg) * minutos * 0.1
                val calorias = peso * minutos * 0.1
                tvResultado.text = "Has quemado aproximadamente %.2f calorías".format(calorias)
            } else {
                tvResultado.text = "Por favor ingresa valores válidos"
            }
        }
    }
}