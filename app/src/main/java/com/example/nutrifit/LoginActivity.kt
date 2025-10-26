package com.example.nutrifit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.data.UserDBHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = UserDBHelper(this)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvIrRegister = findViewById<TextView>(R.id.tvIrRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val db = dbHelper.readableDatabase
                val query = "SELECT * FROM usuarios WHERE email = ? AND password = ?"
                val cursor = db.rawQuery(query, arrayOf(email, password))

                if (cursor.moveToFirst()) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                    val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
                    sharedPref.edit().putString("email_usuario", email).apply()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
            } else {
                    Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
                cursor.close()
                db.close()
            }
        }

        tvIrRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
}