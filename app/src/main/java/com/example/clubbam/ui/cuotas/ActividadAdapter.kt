package com.example.clubbam.ui.cuotas
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.card.MaterialCardView
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.clubbam.R
import com.example.clubbam.data.DBHelper
import com.example.clubbam.model.Actividad
import com.google.android.material.button.MaterialButton
import com.example.clubbam.ui.cuotas.ComprobantePagoActivity
import org.w3c.dom.Text


class ActividadAdapter(
    private val actividadList: List<Actividad>,
    private val nroNoSocio: Int
) : RecyclerView.Adapter<ActividadAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.cardActividad)
        val tvNombreActividad: TextView = itemView.findViewById(R.id.tvNombreActividad)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvDia: TextView = itemView.findViewById(R.id.tvDia)
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
        holder.tvDescripcion.text   = actividad.descripcion
        holder.tvHorario.text       = actividad.horario
        holder.tvDia.text           = actividad.dia
        holder.tvCosto.text         = actividad.costo.toString()
        holder.tvCupo.text          = actividad.cupos.toString()


        val sinCupos = actividad.cupos <= 0
        holder.btnPagar.isEnabled = !sinCupos
        holder.btnPagar.alpha = if (sinCupos) 0.5f else 1f

        holder.btnPagar.setOnClickListener {
            val context = holder.itemView.context
            val db = DBHelper(context)

            val nroPago = db.registrarPagoActividad(
                noSocio = nroNoSocio,
                actividad = actividad.NroActividad,
                monto = actividad.costo
            )

            when {
                nroPago == -2 -> {
                    Toast.makeText(context, "Ya pagaste esta actividad hoy. Intentalo mañana.", Toast.LENGTH_SHORT).show()
                }
                nroPago == -4 -> {
                    Toast.makeText(context, "Sin cupos disponibles para esta actividad.", Toast.LENGTH_SHORT).show()
                }
                nroPago < 0 -> {
                    Toast.makeText(context, "No se pudo registrar el pago (código $nroPago).", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // OK → abrir comprobante
                    val intent = Intent(context, ComprobantePagoActivity::class.java).apply {
                        putExtra("tipo", "actividad")
                        putExtra("nroPagoActividad", nroPago)
                        putExtra("nuevoCliente", false)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int = actividadList.size
}