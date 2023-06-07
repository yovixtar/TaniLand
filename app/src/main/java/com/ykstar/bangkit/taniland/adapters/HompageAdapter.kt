package com.ykstar.bangkit.taniland.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.models.LahanModel

class HomeAdapter(private val lahanList: List<LahanModel>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_lahan_gambar)
        val nameTextView: TextView = itemView.findViewById(R.id.item_lahan_nama)
        val addressTextView: TextView = itemView.findViewById(R.id.item_lahan_alamat)
        val areaTextView: TextView = itemView.findViewById(R.id.item_lahan_luas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lahan_item, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val lahan = lahanList[position]

        Glide.with(holder.itemView.context)
            .load(lahan.photo)
            .into(holder.imageView)

        holder.nameTextView.text = lahan.nama
        holder.addressTextView.text = lahan.alamat
        holder.areaTextView.text =
            holder.itemView.context.getString(R.string.item_lahan_luas, lahan.luas.toString())
    }

    override fun getItemCount() = lahanList.size
}

