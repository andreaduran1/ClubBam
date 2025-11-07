package com.example.clubbam.ui.perfil

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubbam.ui.main.MainActivity
import com.example.clubbam.R
import com.google.android.material.appbar.MaterialToolbar

class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.perfil)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            val session = com.example.clubbam.data.SessionManager(this)

            val tvUserName = findViewById<TextView>(R.id.tvUserName)
            val tvNombreApellido = findViewById<TextView>(R.id.tvNombreApellido)
            val tvNombre = findViewById<TextView>(R.id.tvNombreCarnet)
            val tvApellido = findViewById<TextView>(R.id.tvApellido)
            val tvEmail = findViewById<TextView>(R.id.tvEmail)

            val usuario = session.getUsuario()
            val nombre = session.getNombre()
            val apellido = session.getApellido()
            val email = session.getEmail()

            tvNombreApellido.text = "$nombre $apellido"
            tvUserName.text = "$usuario"
            tvNombre.text  = "$nombre"
            tvApellido.text  = "$apellido"
            tvEmail.text = "$email"
            insets
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Ocultar título por defecto de la Toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Acción del botón de navegación (flecha izquierda)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener{
           AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí") { _, _ ->
                    val session = com.example.clubbam.data.SessionManager(this)
                    session.clear()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
        }
            .setNegativeButton("No", null)
            .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

   /* override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                // Acción al tocar el ícono de perfil
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
                true   // indico que ya procesé el evento
            }

            else -> super.onOptionsItemSelected(item)
        }
    }*/
}