package com.example.nutrifit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.data.UserDBHelper
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = UserDBHelper(this)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val tvIrLogin = findViewById<TextView>(R.id.tvIrLogin)

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            when {
                nombre.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ->
                    showToast("Por favor completa todos los campos")

                !nombre.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) ->
                    showToast("El nombre solo puede contener letras")

                !email.endsWith("@gmail.com") ->
                    showToast("El correo debe terminar en @gmail.com")

                !isValidPassword(password) ->
                    showToast("La contraseña debe tener al menos una mayúscula y un número")

                password != confirmPassword ->
                    showToast("Las contraseñas no coinciden")

                else -> {
                    val registrado = dbHelper.insertUser(nombre, email, password)

                    if (registrado) {
                        showToast("Usuario registrado correctamente")
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        showToast("El correo ya está registrado")
                    }
                }
            }
        }

        tvIrLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[0-9]).{6,}$")
        return pattern.matcher(password).matches()
    }
}
