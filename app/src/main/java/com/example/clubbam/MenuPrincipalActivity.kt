package com.example.clubbam

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.validacionDoc)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Ocultar título por defecto de la Toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //Ingresar nuevo cliente--------------
        var btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        btnRegistrar.setOnClickListener {
            val intent = Intent(this, ValidacionDoc::class.java)
            intent.putExtra("nuevoCliente", true)
            startActivity(intent)

        }
        //Pagar Cuota------------------------
        var btnPagarCuota = findViewById<Button>(R.id.btnPagarCuota)
        btnPagarCuota.setOnClickListener {
            val intent = Intent(this, ValidacionDoc::class.java)
            intent.putExtra("nuevoCliente", false)
            startActivity(intent)
        }
        //Pagar Actividad---------------------
        var btnPagarActividad = findViewById<Button>(R.id.btnPagarActividad)
        btnPagarActividad.setOnClickListener {
            val intent = Intent(this, PagarActividad::class.java)
            startActivity(intent)
        }
        //Listar Cuotas----------------------
        var btnListarCuotas = findViewById<Button>(R.id.btnListarCuotas)
        btnListarCuotas.setOnClickListener {
            val intent = Intent(this, ListarCuotas::class.java)
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
                val intent = Intent(this, Perfil::class.java)
                startActivity(intent)
                true   // indico que ya procesé el evento
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}