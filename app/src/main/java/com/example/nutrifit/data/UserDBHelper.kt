package com.example.nutrifit.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDBHelper(context: Context) : SQLiteOpenHelper(context, "NutriFitDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    // Insertar nuevo usuario
    fun insertUser(nombre: String, email: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("email", email)
            put("password", password)
        }
        val result = db.insert("usuarios", null, values)
        db.close()
        return result != -1L
    }

    // Verificar usuario para login
    fun checkUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE email = ? AND password = ?",
            arrayOf(email, password)
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }

    // Obtener datos de usuario por email (para el perfil)
    fun getUserByEmail(email: String): Map<String, String>? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE email = ?", arrayOf(email))
        return if (cursor.moveToFirst()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val emailUsuario = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            cursor.close()
            db.close()
            mapOf("nombre" to nombre, "email" to emailUsuario, "password" to password)
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    // Actualizar datos de usuario
    fun updateUser(email: String, nuevoNombre: String, nuevaPassword: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nuevoNombre)
            put("password", nuevaPassword)
        }
        val result = db.update("usuarios", values, "email = ?", arrayOf(email))
        db.close()
        return result > 0
    }

    // Eliminar usuario
    fun deleteUser(email: String): Boolean {
        val db = writableDatabase
        val result = db.delete("usuarios", "email = ?", arrayOf(email))
        db.close()
        return result > 0
    }
}