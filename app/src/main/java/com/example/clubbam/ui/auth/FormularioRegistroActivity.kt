package com.example.clubbam.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubbam.ui.cuotas.CuotaCarnetActivity
import com.example.clubbam.ui.menu.MenuPrincipalActivity
import com.example.clubbam.R
import com.google.android.material.appbar.MaterialToolbar

class FormularioRegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formulario_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.formularioRegistro)) { v, insets ->
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

        var btnNoSocio = findViewById<Button>(R.id.btnNoSocio)
        btnNoSocio.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }

        var btnSocio = findViewById<Button>(R.id.btnSocio)
        btnSocio.setOnClickListener{
            val intent = Intent(this, CuotaCarnetActivity::class.java)
            startActivity(intent)
        }
    }
}