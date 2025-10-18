package com.example.nutrifit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class PlanItem(
    val dia: String,
    val desayuno: String,
    val almuerzo: String,
    val cena: String
)

class PlanAdapter(private var listaPlanes: MutableList<PlanItem>) :
    RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDia: TextView = itemView.findViewById(R.id.tvDia)
        val tvDesayuno: TextView = itemView.findViewById(R.id.tvDesayuno)
        val tvAlmuerzo: TextView = itemView.findViewById(R.id.tvAlmuerzo)
        val tvCena: TextView = itemView.findViewById(R.id.tvCena)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plan, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val item = listaPlanes[position]
        holder.tvDia.text = item.dia
        holder.tvDesayuno.text = "Desayuno: ${item.desayuno}"
        holder.tvAlmuerzo.text = "Almuerzo: ${item.almuerzo}"
        holder.tvCena.text = "Cena: ${item.cena}"
    }

    override fun getItemCount(): Int = listaPlanes.size

    fun actualizarPlan(nuevoPlan: List<PlanItem>) {
        listaPlanes.clear()
        listaPlanes.addAll(nuevoPlan)
        notifyDataSetChanged()
    }
}