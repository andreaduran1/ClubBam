package com.example.clubbam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
            //Ingresar nuevo cliente--------------
            var btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
            btnRegistrar.setOnClickListener{
                val intent = Intent(this, ValidacionDoc::class.java)
                startActivity(intent)

        }
            //Pagar Cuota------------------------
            var btnPagarCuota = findViewById<Button>(R.id.btnPagarCuota)
            btnPagarCuota.setOnClickListener{
                val intent = Intent(this, ValidacionDoc::class.java)
                startActivity(intent)
        }
            //Pagar Actividad---------------------
            var btnPagarActividad = findViewById<Button>(R.id.btnPagarActividad)
            btnPagarActividad.setOnClickListener{
                val intent = Intent(this, ValidacionDoc::class.java)
                startActivity(intent)
        }
            //Listar Cuotas----------------------
            var btnListarCuotas = findViewById<Button>(R.id.btnListarCuotas)
            btnListarCuotas.setOnClickListener{
                val intent = Intent(this, ValidacionDoc::class.java)
                startActivity(intent)
        }



    }
}