package com.example.clubbam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RecuperarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.validacionDoc)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var btnRecupero = findViewById<Button>(R.id.btnRecupero)
        btnRecupero.setOnClickListener{
            val intent = Intent(this, ConfirmaRecuperoActivity::class.java)
            startActivity(intent)
        }

        var btnTxtInicio = findViewById<TextView>(R.id.txtInicio)
        btnTxtInicio.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)        }
    }
}