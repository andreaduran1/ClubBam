package com.example.clubbam.ui.cuotas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clubbam.ui.perfil.PerfilActivity
import com.example.clubbam.R
import com.google.android.material.appbar.MaterialToolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.clubbam.data.DBHelper
import com.example.clubbam.model.Actividad


class PagarActividadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagar_actividad)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pagarActividad)) { v, insets ->
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
        val dbHelper = DBHelper(this)
/*
        // Insertar varias actividades de ejemplo
        val actividadesEjemplo = listOf(
            Actividad(0, "Yoga", "Clases de yoga relajante", "Lunes 18:00", 15, 5000.0),
            Actividad(0, "Zumba", "Ejercicio con ritmo y música", "Martes 19:00", 20, 4500.0),
            Actividad(0, "Pilates", "Fortalecimiento corporal", "Jueves 17:00", 12, 4800.0)
        )

        for (actividad in actividadesEjemplo) {
            val id = dbHelper.insertActividad(actividad)
            if (id != -1L) {
                android.util.Log.d("DB", "Insertado ${actividad.nombre} con id $id")
            } else {
                android.util.Log.e("DB", "Error insertando ${actividad.nombre}")
            }
        }*/

        val nroNoSocio = intent.getIntExtra("nroNoSocio", -1)
        if (nroNoSocio == -1) {
            Toast.makeText(this, "Falta el número de No Socio", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val actividadesLista = dbHelper.getActividades()
        Log.d("DB", "Actividades en BD: ${actividadesLista.size}")


        // Acá renderizo la lista de actividades
        val rvActividades = findViewById<RecyclerView>(R.id.rvActividades)
        rvActividades.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val adapter = ActividadAdapter(actividadesLista, nroNoSocio)
        rvActividades.adapter = adapter


       /* val actividadesLista = mutableListOf(
            "YOGA",
            "ZUMBA",
            "STREET FIGHTER",
            "PILATES",
            "YOGA",
            "ZUMBA",
            "STREET FIGHTER",

        )
        val adapter = ActividadAdapter(actividadesLista)
        rvActividades.adapter = adapter


        //Botones de actividad
        var btnPagar = findViewById<Button>(R.id.btnPagar)
        btnPagar.setOnClickListener {
            val intent = Intent(this, ComprobantePagoActivity::class.java)
            startActivity(intent)
        }*/

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