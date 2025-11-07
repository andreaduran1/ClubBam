package com.example.clubbam.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubbam.R
import com.example.clubbam.ui.menu.ValidacionDocActivity
import com.example.clubbam.ui.cuotas.ListarCuotasActivity
import com.example.clubbam.ui.cuotas.PagarActividadActivity
import com.example.clubbam.ui.perfil.PerfilActivity
import com.google.android.material.appbar.MaterialToolbar

class MenuPrincipalActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_menu_principal)
        val session = com.example.clubbam.data.SessionManager(this)
        val nombre = session.getNombre()
        val tvSaludo = findViewById<TextView>(R.id.tvSaludo)
        tvSaludo.text = "Hola $nombre,"


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.menuPrincipal)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            insets
            }

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Ocultar título por defecto de la Toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //Ingresar nuevo cliente--------------
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        btnRegistrar.setOnClickListener {
            val intent = Intent(this, ValidacionDocActivity::class.java)
            intent.putExtra("nuevoCliente", true)
            startActivity(intent)

        }
        //Pagar Cuota------------------------
        val btnPagarCuota = findViewById<Button>(R.id.btnPagarCuota)
        btnPagarCuota.setOnClickListener {
            val intent = Intent(this, ValidacionDocActivity::class.java)
            intent.putExtra("nuevoCliente", false)
            startActivity(intent)
        }
        //Pagar Actividad---------------------
        val btnPagarActividad = findViewById<Button>(R.id.btnPagarActividad)
        btnPagarActividad.setOnClickListener {
            val intent = Intent(this, PagarActividadActivity::class.java)
            startActivity(intent)
        }
        //Listar Cuotas----------------------
        val btnListarCuotas = findViewById<Button>(R.id.btnListarCuotas)
        btnListarCuotas.setOnClickListener {
            val intent = Intent(this, ListarCuotasActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                val intent = Intent(this, com.example.clubbam.ui.perfil.PerfilActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}