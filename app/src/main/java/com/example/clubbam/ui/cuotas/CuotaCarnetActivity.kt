package com.example.clubbam.ui.cuotas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubbam.ui.perfil.CarnetActivity
import com.example.clubbam.ui.perfil.PerfilActivity
import com.example.clubbam.R
import com.example.clubbam.ui.menu.MenuPrincipalActivity
import com.google.android.material.appbar.MaterialToolbar
import com.example.clubbam.data.DBHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CuotaCarnetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cuota_carnet)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cuotaCarnet)) { v, insets ->
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

        val nroSocio = intent.getIntExtra("nroSocio", -1)
        val encabezado = findViewById<TextView>(R.id.tvEncabezado)
        encabezado.text = "Elegí una opción para continuar. Socio N° $nroSocio"

        //BASE DE DATOS
        val dbHelper = DBHelper(this)
        val socio = dbHelper.getSocioPorNro(nroSocio)
        if (socio == null) {
            Toast.makeText(this, "Error al obtener el socio", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val btnPagarCuota = findViewById<Button>(R.id.btnPagarCuota)
        btnPagarCuota.setOnClickListener {
            val intent = Intent(this, PagoCuotaActivity::class.java)
            intent.putExtra("nroSocio", nroSocio)
            intent.putExtra("nuevoCliente", true)
            startActivity(intent)
        }

        val btnEmitirCarnet = findViewById<Button>(R.id.btnEmitirCarnet)
        btnEmitirCarnet.setOnClickListener {
            if (socio.vencCuota == socio.fechaIngreso) {
                android.app.AlertDialog.Builder(this)
                    .setTitle("ATENCIÓN")
                    .setMessage("Para completar el registro primero debe abonar la primera cuota.")
                    .setPositiveButton("Aceptar", null)
                    .show()
            } else {
                val intent = Intent(this, CarnetActivity::class.java)
                intent.putExtra("nroSocio", nroSocio)
                startActivity(intent)
            }
        }
        val btnHome = findViewById<FloatingActionButton>(R.id.btnHome)
        btnHome.setOnClickListener {
            if (socio.vencCuota == socio.fechaIngreso) {
                android.app.AlertDialog.Builder(this)
                        .setTitle("ATENCIÓN")
                        .setMessage("Para completar el registro primero debe abonar la primera cuota.")
                        .setPositiveButton("Aceptar", null)
                        .show()
            } else {
                val intent = Intent(this, MenuPrincipalActivity::class.java)
                startActivity(intent)
            }
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
