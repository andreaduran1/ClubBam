package com.example.clubbam.ui.cuotas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubbam.ui.perfil.PerfilActivity
import com.example.clubbam.R
import com.example.clubbam.data.DBHelper
import com.google.android.material.appbar.MaterialToolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.clubbam.ui.menu.MenuPrincipalActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListarCuotasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar_cuotas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.listarCuotas)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
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
        val dbHelper = DBHelper(this)
        val sociosVencCuota = dbHelper.getSociosConVencimientoHoy(dbHelper.readableDatabase)

        //Renderización de la lista de socios
       val rvSociosLista = findViewById<RecyclerView>(R.id.rvSociosLista)
        rvSociosLista.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val adapter = SociosAdapter(sociosVencCuota)
        rvSociosLista.adapter = adapter

        val btnEnviarMail = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnEnviarMail)
        btnEnviarMail.setOnClickListener {
            val seleccionados = sociosVencCuota.filter { it.estaSeleccionado }

            if (seleccionados.isEmpty()) {
                Toast.makeText(this, "No hay socios seleccionados", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Enviando mail a ${seleccionados.size} socios", Toast.LENGTH_SHORT).show()

            }
        }
        val btnHome = findViewById<FloatingActionButton>(R.id.btnHome)
        btnHome.setOnClickListener{
            val intent = Intent(this, MenuPrincipalActivity::class.java)
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
                // Acción al tocar el ícono de perfil
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
                true   // indico que ya procesé el evento
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}