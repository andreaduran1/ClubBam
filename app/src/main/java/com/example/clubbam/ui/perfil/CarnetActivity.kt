package com.example.clubbam.ui.perfil

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubbam.ui.menu.MenuPrincipalActivity
import com.example.clubbam.R
import com.example.clubbam.data.DBHelper
import com.google.android.material.appbar.MaterialToolbar
import android.widget.TextView
import com.example.clubbam.utils.PdfUtils
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CarnetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_carnet)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.carnet)) { v, insets ->
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
        val dbHelper = DBHelper(this)
        val socio = dbHelper.getSocioPorNro(nroSocio) ?: return
        val tvNumero = findViewById<TextView>(R.id.tvNumero)
        tvNumero.text = socio.nroCarnet.toString()
        val tvNombreCarnet = findViewById<TextView>(R.id.tvNombreCarnet)
        tvNombreCarnet.text = "${socio.nombre} ${socio.apellido}"
        val tvNroDni = findViewById<TextView>(R.id.tvNroDni)
        tvNroDni.text = socio.dni.toString()

        val btnDescargar = findViewById<Button>(R.id.btnDescargar)
        val cardCentral = findViewById<MaterialCardView>(R.id.cardCentral)
        btnDescargar.setOnClickListener {
            PdfUtils.generarPDFdesdeCard(this, cardCentral, "carnet_socio $nroSocio")

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