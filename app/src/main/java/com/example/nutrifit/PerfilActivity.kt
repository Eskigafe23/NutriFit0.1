package com.example.nutrifit

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.nutrifit.data.UserDBHelper
import com.google.android.material.navigation.NavigationView

class PerfilActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDBHelper
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnEditar: Button
    private lateinit var btnGuardar: Button
    private lateinit var btnEliminar: Button

    private var emailUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        dbHelper = UserDBHelper(this)

        // Toolbar + menú lateral
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_inicio -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_perfil -> Toast.makeText(this, "Ya estás en Perfil", Toast.LENGTH_SHORT).show()
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

        // Elementos del layout
        etNombre = findViewById(R.id.etNombrePerfil)
        etEmail = findViewById(R.id.etEmailPerfil)
        etPassword = findViewById(R.id.etPasswordPerfil)
        btnEditar = findViewById(R.id.btnEditar)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnEliminar = findViewById(R.id.btnEliminar)

        // Obtener el correo del usuario logueado
        emailUsuario = intent.getStringExtra("email_usuario")

        if (emailUsuario != null) {
            cargarDatosUsuario(emailUsuario!!)
        } else {
            Toast.makeText(this, "No se recibió el correo del usuario", Toast.LENGTH_SHORT).show()
        }

        // Botón EDITAR
        btnEditar.setOnClickListener {
            setEditable(true)
        }

        // Botón GUARDAR CAMBIOS
        btnGuardar.setOnClickListener {
            val nuevoNombre = etNombre.text.toString().trim()
            val nuevaPassword = etPassword.text.toString().trim()

            if (nuevoNombre.isNotEmpty() && nuevaPassword.isNotEmpty() && emailUsuario != null) {
                val actualizado = actualizarUsuario(emailUsuario!!, nuevoNombre, nuevaPassword)
                if (actualizado) {
                    Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                    setEditable(false)
                } else {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnEliminar.setOnClickListener {
            if (emailUsuario != null) {
                // Crear ventana de confirmación
                val dialogView = layoutInflater.inflate(R.layout.activity_confirm, null)

                val dialog = android.app.AlertDialog.Builder(this)
                    .setView(dialogView)
                    .create()

                dialogView.findViewById<Button>(R.id.btnCancelar).setOnClickListener {
                    dialog.dismiss()
                }

                dialogView.findViewById<Button>(R.id.btnConfirmar).setOnClickListener {
                    val eliminado = eliminarUsuario(emailUsuario!!)
                    if (eliminado) {
                        Toast.makeText(this, "Cuenta eliminada correctamente", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show()
                    }
                }

                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
                dialog.show()
            }
        }
    }

    private fun cargarDatosUsuario(email: String) {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE email=?", arrayOf(email))

        if (cursor.moveToFirst()) {
            etNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombre")))
            etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")))
            etPassword.setText(cursor.getString(cursor.getColumnIndexOrThrow("password")))
            etEmail.isEnabled = false // el email no se puede cambiar
        }
        cursor.close()
        db.close()
    }

    private fun actualizarUsuario(email: String, nuevoNombre: String, nuevaPassword: String): Boolean {
        val db = dbHelper.writableDatabase
        val query = "UPDATE usuarios SET nombre=?, password=? WHERE email=?"
        val stmt = db.compileStatement(query)
        stmt.bindString(1, nuevoNombre)
        stmt.bindString(2, nuevaPassword)
        stmt.bindString(3, email)
        val rows = stmt.executeUpdateDelete()
        db.close()
        return rows > 0
    }

    private fun eliminarUsuario(email: String): Boolean {
        val db = dbHelper.writableDatabase
        val query = "DELETE FROM usuarios WHERE email=?"
        val stmt = db.compileStatement(query)
        stmt.bindString(1, email)
        val rows = stmt.executeUpdateDelete()
        db.close()
        return rows > 0
    }

    private fun setEditable(enable: Boolean) {
        etNombre.isEnabled = enable
        etPassword.isEnabled = enable
        btnGuardar.visibility = if (enable) Button.VISIBLE else Button.GONE
        btnEditar.visibility = if (enable) Button.GONE else Button.VISIBLE
    }
}