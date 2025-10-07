package com.example.nutrifit

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class LoginActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = getSharedPreferences("NutriFitPrefs", MODE_PRIVATE)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvIrRegistro = findViewById<TextView>(R.id.tvIrRegistro)

        btnLogin.setOnClickListener {
            val emailIngresado = etEmail.text.toString()
            val passwordIngresado = etPassword.text.toString()

            val emailGuardado = prefs.getString("email", null)
            val passwordGuardada = prefs.getString("password", null)

            if (emailIngresado.isEmpty() || passwordIngresado.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa tus datos", Toast.LENGTH_SHORT).show()
            } else if (emailIngresado == emailGuardado && passwordIngresado == passwordGuardada) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        tvIrRegistro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
}