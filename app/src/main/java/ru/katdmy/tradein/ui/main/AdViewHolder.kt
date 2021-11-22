package ru.katdmy.tradein.ui.main

import Ad
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.katdmy.tradein.R

class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val markTV: TextView = itemView.findViewById(R.id.mark)
    private val modelTV: TextView = itemView.findViewById(R.id.model)
    private val vyearTV: TextView = itemView.findViewById(R.id.vyear)
    private val noteTV: TextView = itemView.findViewById(R.id.note)

    fun onBind(ad: Ad) {
        markTV.text = ad.mark
        modelTV.text = ad.model
        vyearTV.text = ad.vyear.toString()
        noteTV.text = ad.note
    }
}