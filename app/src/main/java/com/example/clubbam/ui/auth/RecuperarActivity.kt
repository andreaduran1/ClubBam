package com.example.clubbam.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubbam.ui.auth.ConfirmaRecuperoActivity
import com.example.clubbam.ui.main.MainActivity
import com.example.clubbam.R
import com.example.clubbam.ui.menu.MenuPrincipalActivity

class RecuperarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recuperar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtUsuario = findViewById<TextView>(R.id.txtUsuario)
        val txtEmail = findViewById<TextView>(R.id.txtEmail)

        val btnRecupero = findViewById<Button>(R.id.btnRecupero)
        btnRecupero.setOnClickListener{
            val usuario = txtUsuario.text.toString()
            val mail = txtEmail.text.toString()

            if (usuario.isEmpty() || mail.isEmpty()){
                Toast.makeText(this, "Alguno de los campos requeridos se encuentra vacio", Toast.LENGTH_SHORT).show()
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                Toast.makeText(this, "El formato del mail no es válido", Toast.LENGTH_SHORT).show()
            }
            else{
                val intent = Intent(this, ConfirmaRecuperoActivity::class.java)
                startActivity(intent)
            }
        }

        val btnTxtInicio = findViewById<TextView>(R.id.txtInicio)
        btnTxtInicio.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)        }
    }
}