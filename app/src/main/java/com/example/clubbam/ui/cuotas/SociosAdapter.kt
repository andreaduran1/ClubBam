package com.example.clubbam.ui.cuotas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clubbam.R
import com.example.clubbam.model.Socio

class SociosAdapter (private val SociosList: List<Socio>) : RecyclerView.Adapter<SociosAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvNumero: TextView = itemView.findViewById(R.id.tvNumero)
        val tvMail: TextView = itemView.findViewById(R.id.tvMail)
        val chkseleccion: CheckBox = itemView.findViewById(R.id.chkseleccion)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val socio = SociosList[position]
        holder.tvNombre.text = socio.nombre + " " + socio.apellido
        holder.tvNumero.text = "Socio N° " + socio.nroCarnet.toString()
        holder.tvMail.text = socio.mail
        holder.chkseleccion.isChecked = socio.estaSeleccionado
        holder.chkseleccion.setOnCheckedChangeListener { _, isChecked ->
            socio.estaSeleccionado = isChecked
        }

    }

    override fun getItemCount(): Int {
        return SociosList.size
    }




}