package com.example.clubbam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import android.view.Menu
import android.view.MenuItem


class ValidacionDoc : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_validacion_doc)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.validacionDoc)) { v, insets ->
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
        val intent = intent
        val nuevoCliente = intent.getBooleanExtra("nuevoCliente", false)

        if (nuevoCliente) {

            var btnVerificar = findViewById<Button>(R.id.btnVerificar)
            btnVerificar.setOnClickListener {
                val intent = Intent(this, FormularioRegistro::class.java)
                intent.putExtra("nuevoCliente", true)
                startActivity(intent)
            }
        } else {
            var btnVerificar = findViewById<Button>(R.id.btnVerificar)
            btnVerificar.setOnClickListener {
                val intent = Intent(this, PagoCuota::class.java)
                intent.putExtra("nuevoCliente", false)
                startActivity(intent)
            }
        }
    }

        //Sobreescritura del menú para el icono de perfil
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

