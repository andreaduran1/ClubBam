package com.example.clubbam.data
import android.content.Context
import android.content.SharedPreferences

class SessionManager (context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    fun saveUsuario(id: Long, usuario: String, nombre: String, apellido: String, email: String) {
        val editor = prefs.edit()
        editor.putLong("id", id)
        editor.putString("usuario", usuario)
        editor.putString("nombre", nombre)
        editor.putString("apellido", apellido)
        editor.putString("email", email)
        editor.apply()
    }

    fun getId(): Int { return prefs.getInt("id", -1) }
    fun getUsuario(): String { return prefs.getString("usuario", "Usuario") ?: "Usuario" }
    fun getNombre(): String { return prefs.getString("nombre", "Juan") ?: "Juan" }
    fun getApellido(): String { return prefs.getString("apellido", "Lopez") ?: "Lopez" }
    fun getEmail(): String { return prefs.getString("email", "usuario@demo.com") ?: "usuario@demo.com" }

    fun clear() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
