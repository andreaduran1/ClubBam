package com.example.clubbam.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubbam.ui.menu.MenuPrincipalActivity
import com.example.clubbam.R
import com.example.clubbam.data.DBHelper
import com.example.clubbam.data.SessionManager
import com.example.clubbam.ui.auth.RecuperarActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtUsuario = findViewById<TextView>(R.id.txtUsuario)
        val editTextTextPassword = findViewById<TextView>(R.id.editTextTextPassword)

        val dbHelper = DBHelper(this)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener{
            val usuario = txtUsuario.text.toString()
            val password = editTextTextPassword.text.toString()

            if (usuario.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Alguno de los campos requeridos se encuentra vacio", Toast.LENGTH_SHORT).show()
            }
            else if (dbHelper.checkLogin(usuario, password)){
                // traer el usuario completo
                val userObj = dbHelper.getUsuarioPorUsuario(usuario)

                if (userObj != null) {
                    // guardo el usuario en la sesion
                    val session = SessionManager(this)
                    session.saveUsuario(userObj.id, userObj.usuario, userObj.nombre, userObj.apellido, userObj.email)
                }
                val intent = Intent(this, MenuPrincipalActivity::class.java)
                startActivity(intent)
                finish() // para evitar volver al login con back
            }
            else{
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        val btnTxtRecupero = findViewById<TextView>(R.id.tvRecupero)
        btnTxtRecupero.setOnClickListener{
            val intent = Intent(this, RecuperarActivity::class.java)
            startActivity(intent)        }

    }
}