package com.example.clubbam.ui.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.AutoCompleteTextView
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.appcompat.app.AlertDialog
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubbam.ui.cuotas.CuotaCarnetActivity
import com.example.clubbam.ui.menu.MenuPrincipalActivity
import com.example.clubbam.R
import com.example.clubbam.data.DBHelper
import com.example.clubbam.ui.cuotas.PagarActividadActivity
import com.google.android.material.appbar.MaterialToolbar
import java.time.LocalDate
import java.time.ZoneId

class FormularioRegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formulario_registro)
        val dni = intent.getIntExtra("dni", -1)
        val autoComplete = findViewById<AutoCompleteTextView>(R.id.autoCompleteGenero)
        val items = listOf("Masculino", "Femenino", "Otro", "Prefiero no decirlo")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        autoComplete.setAdapter(adapter)
        val switchApto = findViewById<SwitchMaterial>(R.id.switchApto)
        val btnNoSocio = findViewById<Button>(R.id.btnNoSocio)
        val btnSocio = findViewById<Button>(R.id.btnSocio)
        btnNoSocio.isEnabled = false
        btnSocio.isEnabled = false
        btnSocio.alpha = 0.9f
        btnNoSocio.alpha = 0.9f




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.formularioRegistro)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            val tvDNI = findViewById<TextView>(R.id.tvDNI)
            tvDNI.text = dni.toString()
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
        //SELECTOR DE FECHA NAC
        val inputFechaNac = findViewById<TextView>(R.id.inputFechaNac)
        inputFechaNac.setOnClickListener {
            val hoy = LocalDate.now()
            val max = hoy.minusYears(10) // máximo permitido (mínimo 10 años de edad)
            val min = LocalDate.of(1900, 1, 1) // límite inferior razonable

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    inputFechaNac.text = selectedDate.toString()
                },
                max.year,
                max.monthValue - 1,
                max.dayOfMonth
            )

            fun LocalDate.toMillis(): Long =
                this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

            // Limitar selección
            datePickerDialog.datePicker.minDate = min.toMillis()
            datePickerDialog.datePicker.maxDate = max.toMillis()

            datePickerDialog.show()
        }

        //HABILITACIÓN DE LOS BOTONES
        switchApto.setOnCheckedChangeListener { _, isChecked ->
            btnNoSocio.isEnabled = isChecked
            btnSocio.isEnabled = isChecked

            val alphaValue = if (isChecked) 1.0f else 0.9f
            btnNoSocio.alpha = alphaValue
            btnSocio.alpha = alphaValue
        }

        //BASE DE DATOS
        val dbHelper = DBHelper(this)

        //Registrar NO SOCIO
        btnNoSocio.setOnClickListener {
            //DATOS DEL CLIENTE
            val nombre = findViewById<TextView>(R.id.inputNombre).text.toString()
            val apellido = findViewById<TextView>(R.id.inputApellido).text.toString()
            val domicilio = findViewById<TextView>(R.id.inputDomicilio).text.toString()
            val mail = findViewById<TextView>(R.id.inputMail).text.toString()
            val numCel = findViewById<TextView>(R.id.inputTelefono).text.toString()
            val fechaNac = findViewById<TextView>(R.id.inputFechaNac).text.toString()
            val genero = autoComplete.text.toString()

            if (nombre.isEmpty() || apellido.isEmpty() || domicilio.isEmpty() || mail.isEmpty() || numCel.isEmpty() || fechaNac.isEmpty() || genero.isEmpty()) {
                Toast.makeText(
                    this,
                    "Alguno de los campos requeridos se encuentran vacío o el formato no es valido",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                val nroNoSocio = dbHelper.registrarNoSocio(
                    nombre,
                    apellido,
                    dni,
                    fechaNac,
                    genero,
                    mail,
                    numCel,
                    domicilio,
                    true
                )
                if (nroNoSocio == -1) {
                    Toast.makeText(this, "Error al registrar el cliente", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    android.app.AlertDialog.Builder(this)
                        .setTitle("REGISTRO EXITOSO")
                        .setMessage("Se ha creado el cliente en la categoría No Socio con el Nro. $nroNoSocio. ¿Desea navegar al pago de actividades?")
                        .setPositiveButton("Sí") { _, _ ->
                            val intent = Intent(this, PagarActividadActivity::class.java)
                            intent.putExtra("nroNoSocio", nroNoSocio)
                            startActivity(intent)
                        }
                        .setNegativeButton("No") { _, _ ->
                            val intent = Intent(this, MenuPrincipalActivity::class.java)
                            startActivity(intent)
                        }
                        .show()
                }
            }
        }


        //REGISTRO SOCIO
        btnSocio.setOnClickListener {
            //DATOS DEL CLIENTE
            val nombre = findViewById<TextView>(R.id.inputNombre).text.toString()
            val apellido = findViewById<TextView>(R.id.inputApellido).text.toString()
            val domicilio = findViewById<TextView>(R.id.inputDomicilio).text.toString()
            val mail = findViewById<TextView>(R.id.inputMail).text.toString()
            val numCel = findViewById<TextView>(R.id.inputTelefono).text.toString()
            val fechaNac = findViewById<TextView>(R.id.inputFechaNac).text.toString()
            val genero = autoComplete.text.toString()
            val fechaIngreso = LocalDate.now().toString()
            val vencCuota = LocalDate.now().toString()


            if (nombre.isEmpty() || apellido.isEmpty() || domicilio.isEmpty() || mail.isEmpty() || numCel.isEmpty() || fechaNac.isEmpty() || genero.isEmpty()) {
                Toast.makeText(
                    this,
                    "Alguno de los campos requeridos se encuentran vacío o el formato no es valido",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                android.app.AlertDialog.Builder(this)
                    .setTitle("ATENCIÓN")
                    .setMessage("Para completar el registro se debe abonar al menos la primer cuota. ¿Desea continuar?")
                    .setPositiveButton("Sí") { _, _ ->
                        val nroSocio = dbHelper.registrarSocio(
                            nombre,
                            apellido,
                            dni,
                            fechaNac,
                            genero,
                            mail,
                            numCel,
                            domicilio,
                            true,
                            fechaIngreso,
                            vencCuota
                        )
                        if (nroSocio == -1) {
                            Toast.makeText(
                                this,
                                "Error al registrar el socio",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            Toast.makeText(
                                this,
                                "Se ha creado el cliente en la categoría Socio con el Nro. $nroSocio",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@FormularioRegistroActivity, CuotaCarnetActivity::class.java)
                            intent.putExtra("nroSocio", nroSocio)
                            intent.putExtra("nuevoCliente", true)
                            startActivity(intent)
                        }
                    }
                    .setNegativeButton("No") { _, _ ->
                        val intent = Intent(this, MenuPrincipalActivity::class.java)
                        startActivity(intent)
                    }
                    .show()
            }
        }
    }
}




