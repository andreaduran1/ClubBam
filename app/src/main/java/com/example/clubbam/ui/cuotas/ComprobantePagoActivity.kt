package com.example.clubbam.ui.cuotas

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
import com.example.clubbam.ui.perfil.PerfilActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import android.widget.TextView
import com.example.clubbam.data.DBHelper
import com.example.clubbam.utils.PdfUtils
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.format.DateTimeFormatter
import java.util.Locale


class ComprobantePagoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_comprobante_pago)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.comprobantePago)) { v, insets ->
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
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
        val nuevoCliente = intent.getBooleanExtra("nuevoCliente", false)
        val nroCuota = intent.getIntExtra("nroCuota", -1)
        val dbHelper = DBHelper(this)
        val datos = dbHelper.getCuotaConSocio(nroCuota)
        val (cuota, socio) = datos ?: return


        //DATOS PARA DIBUJAR EL COMPROBANTE

        val tvNroComprobante = findViewById<TextView>(R.id.tvNroComprobante)
        tvNroComprobante.text = "Nº $nroCuota"

        val tvFechaPago = findViewById<TextView>(R.id.tvFechaPago)
        val fechaPagoStr = cuota.fechaPago?.format(dateFormatter) ?: "No registrada"
        tvFechaPago.text = "Fecha de pago: $fechaPagoStr"

        val tvNombre = findViewById<TextView>(R.id.tvNombreCarnet)
        tvNombre.text = "Nombre: ${socio.nombre} ${socio.apellido}"
        val tvNroDni = findViewById<TextView>(R.id.tvNroDni)
        tvNroDni.text = "DNI: ${socio.dni}"

        val tvImporte = findViewById<TextView>(R.id.txtImporte)
        val importeStr = String.format(Locale.getDefault(), "%.2f", cuota.importe)
        tvImporte.text = "Importe: \$${importeStr}"

        val tvMedioPago = findViewById<TextView>(R.id.txtMedioPago)
        tvMedioPago.text = "Medio de pago: ${cuota.metodoPago}"
        val tvCantCuotas = findViewById<TextView>(R.id.txtCantCuotas)
        tvCantCuotas.text = "Cantidad de cuotas: ${cuota.cantCuotas}"


        val btnHome = findViewById<FloatingActionButton>(R.id.btnHome)
        btnHome.setOnClickListener {
            if (nuevoCliente) {
                android.app.AlertDialog.Builder(this)
                    .setTitle("CARNET DE SOCIO")
                    .setMessage("¿Desea emitir el carnet de socio?")
                    .setPositiveButton("Sí") { _, _ ->
                        val intent = Intent(this, CuotaCarnetActivity::class.java)
                        //intent.putExtra("nuevoCliente", true)
                        intent.putExtra("nroSocio", socio.nroCarnet)
                        startActivity(intent)
                    }
                    .setNegativeButton("No") { _, _ ->
                        val intent = Intent(this, MenuPrincipalActivity::class.java)
                        startActivity(intent)
                    }
                    .show()
            } else {
                val intent = Intent(this, MenuPrincipalActivity::class.java)
                startActivity(intent)
            }
        }

        val btnDescargar = findViewById<Button>(R.id.btnDescargar)
        val cardCentral = findViewById<MaterialCardView>(R.id.cardCentral)
        btnDescargar.setOnClickListener {
            PdfUtils.generarPDFdesdeCard(this, cardCentral, "comprobante_pago $nroCuota")
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