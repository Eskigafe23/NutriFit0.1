package com.example.nutrifit

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 🔹 Configurar Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // 🔹 Configurar DrawerLayout (menú lateral)
        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 🔹 Configurar NavigationView
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_inicio -> {
                    Toast.makeText(this, "Ya estás en inicio", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_perfil -> {
                    Toast.makeText(this, "Abrir perfil (en construcción)", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_cerrar_sesion -> {
                    Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        // 🔹 Botones principales del Home
        val btnPlan = findViewById<Button>(R.id.btnPlan)
        val btnTips = findViewById<Button>(R.id.btnTips)
        val btnCalorias = findViewById<Button>(R.id.btnCalorias)

        // 🔸 Acción del botón "Ver Plan Alimenticio"
        btnPlan.setOnClickListener {
            val intent = Intent(this, PlanActivity::class.java)
            startActivity(intent)
        }

        // 🔸 Acción del botón "Tips de Nutrición"
        btnTips.setOnClickListener {
            val intent = Intent(this, TipsActivity::class.java)
            startActivity(intent)
        }

        // 🔸 Acción del botón "Calculadora de Calorías"
        btnCalorias.setOnClickListener {
            val intent = Intent(this, CaloriasActivity::class.java)
            startActivity(intent)
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