package com.example.clubbam.ui.cuotas

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge

import com.google.android.material.button.MaterialButtonToggleGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.example.clubbam.ui.perfil.PerfilActivity
import com.example.clubbam.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.example.clubbam.data.DBHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView

import java.time.LocalDate
import java.util.Locale


class PagoCuotaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pago_cuota)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pagoCuota)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Referencias de vistas
        val etTotal = findViewById<TextInputEditText>(R.id.inputImporteTotal)
        val txtCuotas = findViewById<TextView>(R.id.txtCuotas)
        val tgCuotas = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroup1)
        val etImporte = findViewById<TextInputEditText>(R.id.tvImporte)
        val importeTexto = etImporte.text.toString().replace("$", "").trim()
        val importe = importeTexto.toDoubleOrNull() ?: 0.0
        val tgMedio = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroupMedioPago)



        // Ocultar título por defecto de la Toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Acción del botón de navegación (flecha izquierda)
        toolbar.setNavigationOnClickListener {
            finish()

        }

        val nuevoCliente = intent.getBooleanExtra("nuevoCliente", false)
        val nroSocio = intent.getIntExtra("nroSocio", -1)
        val tvNroSocio = findViewById<TextView>(R.id.tvNroSocio)
        tvNroSocio.text = "Socio N° $nroSocio"


        //BASE DE DATOS
        val dbHelper = DBHelper(this)
        val socio = dbHelper.getSocioPorNro(nroSocio)
        if (socio == null) {
            Toast.makeText(this, "Error al obtener el socio", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val txtVencimiento = findViewById<MaterialTextView>(R.id.txtVencFecha)

        // Convertir el texto a LocalDate y luego formatear a dd/MM/yyyy
        try {
            val formatterIn = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formatterOut = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")

            val vencimientoDate = java.time.LocalDate.parse(socio.vencCuota.toString(), formatterIn)
            val vencimientoFormateado = vencimientoDate.format(formatterOut)

            txtVencimiento.setText(vencimientoFormateado)

            // Hacer el campo de solo lectura
            txtVencimiento.keyListener = null
            txtVencimiento.isFocusable = false
            txtVencimiento.isCursorVisible = false
            txtVencimiento.isLongClickable = false
            txtVencimiento.setTextIsSelectable(false)
        } catch (e: Exception) {
            txtVencimiento.setText("Fecha inválida")
        }

        val toggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroupMedioPago)
        // Por defecto Efectivo
        tgMedio.check(R.id.btnEfectivo)
        var medioPago = "Efectivo" //por defecto
        var cuotasSeleccionadas = 1

        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                medioPago = when (checkedId) {
                    R.id.btnTarjeta -> "Tarjeta"
                    R.id.btnEfectivo -> "Efectivo"
                    else -> ""
                }
            }
        }

        // Helper: parsea el importe ingresado
        fun leerImporteBase(): Double {
            val txt = etImporte.text?.toString()?.replace("$", "")?.trim().orEmpty()
            return txt.toDoubleOrNull() ?: 0.0
        }

        // Helper: calcula y pinta el total
        fun actualizarTotal() {
            val base = leerImporteBase()
            val aplicaInteres = (medioPago == "Tarjeta" && (cuotasSeleccionadas == 9 || cuotasSeleccionadas == 12))
            val total = if (aplicaInteres) base * 1.20 else base
            etTotal.setText(if (total == 0.0) "" else String.format(Locale.getDefault(), "%.2f", total))
        }

        // Mostrar/ocultar cuotas según medio de pago
        tgMedio.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            medioPago = when (checkedId) {
                R.id.btnTarjeta -> "Tarjeta"
                R.id.btnEfectivo -> "Efectivo"
                else -> medioPago
            }

            val mostrarCuotas = (medioPago == "Tarjeta")
            txtCuotas.visibility = if (mostrarCuotas) View.VISIBLE else View.GONE
            tgCuotas.visibility = if (mostrarCuotas) View.VISIBLE else View.GONE

            if (mostrarCuotas) {
                if (tgCuotas.checkedButtonId == View.NO_ID) {
                    tgCuotas.check(R.id.btn_1cuota)
                }
            }
            // Si cambia a efectivo, reseteo cuotas (opcional) y recalculo
            if (!mostrarCuotas) cuotasSeleccionadas = 1
            actualizarTotal()
        }

        // Capturar selección de cuotas (solo si es Tarjeta)
        tgCuotas.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            cuotasSeleccionadas = when (checkedId) {
                R.id.btn_1cuota -> 1
                R.id.btn_3cuotas -> 3
                R.id.btn_6cuotas -> 6
                R.id.btn_9cuotas -> 9
                R.id.btn_12cuotas -> 12
                else -> 1
            }
            actualizarTotal()
        }

       // Cuando cambia el importe base, recalcular total
        etImporte.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { actualizarTotal() }
        })

      // Asegurar que el total no sea editable:
        etTotal.keyListener = null


        val toggleGroupCuotas = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroup1)
        var cantidadCuotas = 1 // valor por defecto

        toggleGroupCuotas.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                cantidadCuotas = when (checkedId) {
                    R.id.btn_1cuota -> 1
                    R.id.btn_3cuotas -> 3
                    R.id.btn_6cuotas -> 6
                    R.id.btn_9cuotas -> 9
                    R.id.btn_12cuotas -> 12
                    else -> 1
                }
            }
        }

        val btnPagar = findViewById<Button>(R.id.btnPagar)
        btnPagar.setOnClickListener {
            val tilImporte = findViewById<TextInputLayout>(R.id.textInputImporte)
            val tgCuotas = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroup1)

            // Validación de importe
            val importeTxt = etImporte.text?.toString()?.trim().orEmpty()
            val importeBase = importeTxt.replace("$", "").replace(",", ".").toDoubleOrNull()

            if (importeTxt.isEmpty()) {
                tilImporte.error = "Ingresá un importe"
                etImporte.requestFocus()
                return@setOnClickListener
            }
            if (importeBase == null || importeBase <= 0.0) {
                tilImporte.error = "Importe inválido"
                etImporte.requestFocus()
                return@setOnClickListener
            }
            tilImporte.error = null

            // Validación de cuotas si es Tarjeta
            if (medioPago == "Tarjeta") {
                val checkedCuotaId = tgCuotas.checkedButtonId

                if (checkedCuotaId == View.NO_ID) {
                    Snackbar.make(tgCuotas, "Seleccioná la cantidad de cuotas", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Recalcular total
            val totalPagar = etTotal.text?.toString()?.replace(",", ".")?.toDoubleOrNull()
                ?: importeBase

            val nroCuota = dbHelper.registrarCuota(
                socio.nroCarnet,
                socio.vencCuota.toString(),
                LocalDate.now().toString(),
                totalPagar,
                medioPago,
                cuotasSeleccionadas
            )

            val nuevaFecha = socio.vencCuota.plusMonths(1)
            val filas = dbHelper.updateVencCuota(socio.nroCarnet, nuevaFecha)

            if (filas > 0) {
                socio.vencCuota = nuevaFecha
                Toast.makeText(this, "Cuota pagada. Próximo vencimiento: ${socio.vencCuota}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al actualizar la cuota", Toast.LENGTH_SHORT).show()
            }

            val intent = Intent(this, ComprobantePagoActivity::class.java).apply {
                putExtra("nroCuota", nroCuota)
                putExtra("nuevoCliente", nuevoCliente)
            }
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