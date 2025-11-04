package com.example.clubbam.ui.menu

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.clubbam.ui.menu.MenuPrincipalActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubbam.R
import com.example.clubbam.data.DBHelper
import com.example.clubbam.ui.auth.FormularioRegistroActivity
import com.example.clubbam.ui.cuotas.PagarActividadActivity
import com.example.clubbam.ui.cuotas.PagoCuotaActivity
import com.example.clubbam.ui.perfil.PerfilActivity
import com.google.android.material.appbar.MaterialToolbar

class ValidacionDocActivity : AppCompatActivity() {
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
        //val nuevoCliente = intent.getBooleanExtra("nuevoCliente", false)


        val btnVerificar = findViewById<Button>(R.id.btnVerificar)
        btnVerificar.setOnClickListener {
            val dniString = findViewById<EditText>(R.id.inputDNI).text.toString()
            if (dniString.isEmpty()) {
                Toast.makeText(this, "Este campo no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val dni = dniString.toIntOrNull()
            if (dni == null) {
                Toast.makeText(this, "DNI inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val dbHelper = DBHelper(this)
            val (existe, esSocio, nro) = dbHelper.verificarDni(dni)

            if (existe) {
                if (esSocio) {
                    AlertDialog.Builder(this)
                        .setTitle("ATENCIÓN")
                        .setMessage("El DNI ingresado corresponde a un cliente de tipo Socio nro.: $nro. ¿Desea navegar al pago de cuotas?")
                        .setPositiveButton("Sí") { _, _ ->
                            val intent = Intent(this, PagoCuotaActivity::class.java)
                            intent.putExtra("dni", dni)
                            startActivity(intent)
                        }

                        .setNegativeButton("No") { _, _ ->

                        }
                        .show()
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("ATENCIÓN")
                        .setMessage("El DNI ingresado corresponde a un cliente de tipo No Socio nro.: $nro. ¿Desea navegar al pago de actividades?")
                        .setPositiveButton("Sí") { _, _ ->
                            val intent = Intent(this, PagarActividadActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("No") { _, _ ->

                        }
                        .show()
                }
            } else {
                val intent = Intent(this, FormularioRegistroActivity::class.java)
                intent.putExtra("dni", dni)
                startActivity(intent)
            }


        }


        val btnHome = findViewById<FloatingActionButton>(R.id.btnHome)
        btnHome.setOnClickListener{
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
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
                    val intent = Intent(this, PerfilActivity::class.java)
                    startActivity(intent)
                    true   // indico que ya procesé el evento
                }

                else -> super.onOptionsItemSelected(item)
            }
        }
    }
