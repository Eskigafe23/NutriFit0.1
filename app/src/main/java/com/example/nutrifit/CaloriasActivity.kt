package com.example.nutrifit

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class CaloriasActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calorias)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Recuperar correo desde SharedPreferences
        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        val emailUsuario = sharedPref.getString("email_usuario", null)

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_inicio -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_perfil -> {
                    val intentPerfil = Intent(this, PerfilActivity::class.java)
                    intentPerfil.putExtra("email_usuario", emailUsuario)
                    startActivity(intentPerfil)
                }
                R.id.nav_cerrar_sesion -> {
                    sharedPref.edit().clear().apply()
                    Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        // 🔹 Elementos de la calculadora
        val etEdad = findViewById<EditText>(R.id.etEdad)
        val etPeso = findViewById<EditText>(R.id.etPeso)
        val etAltura = findViewById<EditText>(R.id.etAltura)
        val rgSexo = findViewById<RadioGroup>(R.id.rgGenero)
        val spinner = findViewById<Spinner>(R.id.spnActividad)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val cardResultado = findViewById<CardView>(R.id.cardResultado)

        // 🔹 Opciones del spinner
        val opciones = arrayOf("Poco activo", "Moderado", "Muy activo")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // 🔹 Botón Calcular
        btnCalcular.setOnClickListener {
            val edadStr = etEdad.text.toString()
            val pesoStr = etPeso.text.toString()
            val alturaStr = etAltura.text.toString()

            // Validaciones de campos vacíos
            if (edadStr.isEmpty() || pesoStr.isEmpty() || alturaStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Edad: solo enteros positivos
            if (!edadStr.matches(Regex("\\d+"))) {
                Toast.makeText(this, "La edad solo puede contener números enteros", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val edad = edadStr.toInt()
            if (edad <= 0) {
                Toast.makeText(this, "Ingresa una edad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Peso y altura: solo decimales positivos (al menos un dígito antes y después del punto)
            if (!pesoStr.matches(Regex("\\d+\\.\\d+"))) {
                Toast.makeText(this, "Ingresa el peso en formato decimal (ej: 70.0)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!alturaStr.matches(Regex("\\d+\\.\\d+"))) {
                Toast.makeText(this, "Ingresa la altura en formato decimal (ej: 1.75)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val peso = pesoStr.toDouble()
            val altura = alturaStr.toDouble()

            // Validaciones adicionales
            if (peso <= 0 || altura <= 0) {
                Toast.makeText(this, "Peso y altura deben ser mayores a 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Determinar sexo
            val esHombre = rgSexo.checkedRadioButtonId == R.id.rbMasculino
            val actividad = spinner.selectedItem.toString()

            // Calcular calorías
            val caloriasBase = if (esHombre)
                66 + (13.7 * peso) + (5 * altura) - (6.8 * edad)
            else
                655 + (9.6 * peso) + (1.8 * altura) - (4.7 * edad)

            val factor = when (actividad) {
                "Poco activo" -> 1.2
                "Moderado" -> 1.55
                else -> 1.9
            }

            val resultado = caloriasBase * factor

            // Mostrar resultado
            cardResultado.visibility = View.VISIBLE
            tvResultado.text = "Necesitas %.0f calorías diarias".format(resultado)
        }
    }

    // 🔹 Necesario para que funcione el botón del menú lateral (hamburguesa)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}