package ru.katdmy.tradein.ui.main

import Ad
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.katdmy.tradein.R

class AdsAdapter: RecyclerView.Adapter<AdViewHolder>() {

    private var ads: MutableList<Ad> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.ad_viewholder, parent, false)
        return AdViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        holder.onBind(ads[position])
    }

    override fun getItemCount(): Int = ads.size

    fun loadAds(adsToLoad: List<Ad>) {
        ads.clear()
        ads.addAll(adsToLoad)
        notifyItemRangeChanged(0, itemCount)
    }
}