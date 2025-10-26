package com.example.nutrifit

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class PlanActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlanAdapter

    private val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    private val desayunos = listOf("Avena con frutas", "Pan integral con palta", "Huevos revueltos", "Yogurt con avena")
    private val almuerzos = listOf("Pollo con ensalada", "Pescado al horno", "Lentejas con arroz", "Arroz integral con verduras")
    private val cenas = listOf("Sopa de verduras", "Ensalada de frutas", "Batido de plátano", "Tostadas integrales")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)

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
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
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

        recyclerView = findViewById(R.id.recyclerPlan)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PlanAdapter(generarPlanAleatorio().toMutableList())
        recyclerView.adapter = adapter

        val btnGenerar = findViewById<Button>(R.id.btnGenerar)
        btnGenerar.setOnClickListener {
            adapter.actualizarPlan(generarPlanAleatorio())
        }
    }

    private fun generarPlanAleatorio(): List<PlanItem> {
        return dias.map { dia ->
            PlanItem(
                dia,
                desayunos.random(),
                almuerzos.random(),
                cenas.random()
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}