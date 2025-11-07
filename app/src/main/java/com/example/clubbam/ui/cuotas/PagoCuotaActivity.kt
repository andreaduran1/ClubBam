package com.example.clubbam.ui.cuotas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

import java.time.LocalDate



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
        val etImporte = findViewById<TextInputEditText>(R.id.tvImporte)
        val importeTexto = etImporte.text.toString().replace("$", "").trim()
        val importe = importeTexto.toDoubleOrNull() ?: 0.0

        //BASE DE DATOS
        val dbHelper = DBHelper(this)
        val socio = dbHelper.getSocioPorNro(nroSocio)
        if (socio == null) {
            Toast.makeText(this, "Error al obtener el socio", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val toggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroupMedioPago)
        var medioPago = "Efectivo" //por defecto

        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                medioPago = when (checkedId) {
                    R.id.btnTarjeta -> "Tarjeta"
                    R.id.btnEfectivo -> "Efectivo"
                    else -> ""
                }
            }
        }
        val toggleGroupCuotas = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroup1)
        var cantidadCuotas = 1 // valor por defecto, si querés

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
        btnPagar.setOnClickListener{
            //registra la cuota
            val nroCuota = dbHelper.registrarCuota(
                socio.nroCarnet,
                socio.vencCuota.toString(),
                LocalDate.now().toString(),
                importe,
                medioPago,
                cantidadCuotas
            )

            val nuevaFecha = socio.vencCuota.plusMonths(1)
            val filas = dbHelper.updateVencCuota(socio.nroCarnet, nuevaFecha)

            if (filas > 0) {
                socio.vencCuota = nuevaFecha
                Toast.makeText(this, "Cuota pagada con éxito. Próximo vencimiento: ${socio.vencCuota}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al actualizar la cuota", Toast.LENGTH_SHORT).show()
            }
                val intent = Intent(this, ComprobantePagoActivity::class.java)
                intent.putExtra("nroCuota", nroCuota)
                if (nuevoCliente){
                    intent.putExtra("nuevoCliente", true)}
                else{
                    intent.putExtra("nuevoCliente", false)}
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