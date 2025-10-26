package com.example.clubbam.ui.cuotas
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.card.MaterialCardView
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.example.clubbam.R
import com.example.clubbam.model.Actividad
import com.google.android.material.button.MaterialButton
import com.example.clubbam.ui.cuotas.ComprobantePagoActivity




class ActividadAdapter (private val actividadList: List<Actividad>): RecyclerView.Adapter<ActividadAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val cardView: MaterialCardView = itemView.findViewById(R.id.cardActividad)
        val tvNombreActividad: TextView = itemView.findViewById(R.id.tvNombreActividad)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvHorario: TextView = itemView.findViewById(R.id.tvHorario)
        val tvCosto: TextView = itemView.findViewById(R.id.tvCosto)
        val tvCupo: TextView = itemView.findViewById(R.id.tvCupo)
        val btnPagar: TextView = itemView.findViewById(R.id.btnPagar)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actividad = actividadList[position]
        holder.tvNombreActividad.text = actividad.nombre
        holder.tvDescripcion.text = actividad.descripcion
        holder.tvHorario.text = actividad.horario
        holder.tvCosto.text = actividad.cuota.toString()
        holder.tvCupo.text = actividad.cupo.toString()
        holder.btnPagar.setOnClickListener {
            val context = holder.itemView.context // Obtiene el contexto del ViewHolder
            val intent = Intent(context, ComprobantePagoActivity::class.java)
            context.startActivity(intent)
        }

    }
    override fun getItemCount(): Int {
        return actividadList.size
    }



}