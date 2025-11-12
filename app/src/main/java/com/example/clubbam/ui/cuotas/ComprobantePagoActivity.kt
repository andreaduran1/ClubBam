package com.example.clubbam.ui.cuotas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubbam.ui.menu.MenuPrincipalActivity
import com.example.clubbam.R
import com.example.clubbam.ui.perfil.PerfilActivity
import com.google.android.material.appbar.MaterialToolbar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.clubbam.data.DBHelper
import com.example.clubbam.utils.PdfUtils
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


class ComprobantePagoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_comprobante_pago)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.comprobantePago)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom); insets
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)

        val dateOut = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
        val nuevoCliente = intent.getBooleanExtra("nuevoCliente", false)
        val tipo = intent.getStringExtra("tipo") ?: "cuota"

        // Vistas
        val tvNroComprobante = findViewById<TextView>(R.id.tvNroComprobante)
        val tvFechaPago = findViewById<TextView>(R.id.tvFechaPago)
        val tvNombre = findViewById<TextView>(R.id.tvNombreCarnet)
        val tvNroDni = findViewById<TextView>(R.id.tvNroDni)
        val tvImporte = findViewById<TextView>(R.id.txtImporte)
        val tvMedioPago = findViewById<TextView>(R.id.txtMedioPago)
        val tvCantCuotas = findViewById<TextView>(R.id.txtCantCuotas)
        val btnDescargar = findViewById<Button>(R.id.btnDescargar)
        val cardCentral = findViewById<MaterialCardView>(R.id.cardCentral)
        val btnCerrar = findViewById<FloatingActionButton>(R.id.btnCerrar)

        val dbHelper = DBHelper(this)

        if (tipo == "cuota") {
            // Comprobante de CUOTA (SOCIO)
            val nroCuota = intent.getIntExtra("nroCuota", -1)
            val datos = dbHelper.getCuotaConSocio(nroCuota)
            if (datos == null) {
                Toast.makeText(this, "No se encontró la cuota $nroCuota", Toast.LENGTH_SHORT).show()
                finish(); return
            }
            val (cuota, socio) = datos

            tvNroComprobante.text = "Nº $nroCuota"
            val fechaPagoStr = cuota.fechaPago?.format(dateOut) ?: "No registrada"
            tvFechaPago.text = "Fecha de pago: $fechaPagoStr"
            tvNombre.text = "Nombre: ${socio.nombre} ${socio.apellido}"
            tvNroDni.text = "DNI: ${socio.dni}"
            tvImporte.text = "Importe: \$${String.format(Locale.getDefault(), "%.2f", cuota.importe)}"
            tvMedioPago.text = "Medio de pago: ${cuota.metodoPago}"
            tvCantCuotas.text = "Cantidad de cuotas: ${cuota.cantCuotas}"
            tvMedioPago.visibility = View.VISIBLE
            tvCantCuotas.visibility = View.VISIBLE

            btnDescargar.setOnClickListener {
                PdfUtils.generarPDFdesdeCard(this, cardCentral, "comprobante_cuota_$nroCuota")
            }

            btnCerrar.setOnClickListener {
                if (nuevoCliente) {
                    AlertDialog.Builder(this)
                        .setTitle("CARNET DE SOCIO")
                        .setMessage("¿Desea emitir el carnet de socio?")
                        .setPositiveButton("Sí") { _, _ ->
                            val i = Intent(this, CuotaCarnetActivity::class.java)
                            i.putExtra("nroSocio", socio.nroCarnet)
                            startActivity(i)
                        }
                        .setNegativeButton("No") { _, _ ->
                            startActivity(Intent(this, MenuPrincipalActivity::class.java))
                        }
                        .show()
                } else {
                    startActivity(Intent(this, MenuPrincipalActivity::class.java))
                }
            }

        } else {
            // Comprobante de ACTIVIDAD (NO SOCIO)
            val nroPago = intent.getIntExtra("nroPagoActividad", -1)
            val triple = dbHelper.getPagoActividad(nroPago)
            if (triple == null) {
                Toast.makeText(this, "No se encontró el pago $nroPago", Toast.LENGTH_SHORT).show()
                finish(); return
            }
            val (pago, noSocio, nombreActividad) = triple

            // Extraer valores del Map
            val monto = (pago["monto"] as? Double) ?: 0.0
            val fechaPagoIso = (pago["fechaPago"] as? String).orEmpty() // "yyyy-MM-dd"
            val fechaPagoLocal = try {
                LocalDate.parse(fechaPagoIso).format(dateOut) } catch (_: Exception) { fechaPagoIso }

            val nombreCompleto = "${noSocio["nombre"]} ${noSocio["apellido"]}"
            val dni = noSocio["dni"]?.toString() ?: "-"

            tvNroComprobante.text = "Nº ACT-$nroPago"
            tvFechaPago.text = "Fecha de pago: $fechaPagoLocal"
            tvNombre.text = "Nombre: $nombreCompleto"
            tvNroDni.text = "DNI: $dni"
            tvImporte.text = "Importe: \$${String.format(Locale.getDefault(), "%.2f", monto)}"

            // Para actividad: se oculta cuotas y se reutiliza "Medio de pago" para mostrar la actividad
            tvCantCuotas.visibility = View.GONE
            tvMedioPago.visibility = View.VISIBLE
            val actLabel = nombreActividad ?: (pago["actividad"]?.toString() ?: "-")
            tvMedioPago.text = "Actividad: $actLabel"

            btnDescargar.setOnClickListener {
                PdfUtils.generarPDFdesdeCard(this, cardCentral, "comprobante_actividad_$nroPago")
            }

            btnCerrar.setOnClickListener {
                startActivity(Intent(this, MenuPrincipalActivity::class.java))
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
                startActivity(Intent(this, PerfilActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
