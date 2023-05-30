package com.ykstar.bangkit.taniland.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ykstar.bangkit.taniland.R

data class Lahan(
    val imageResId: Int,
    val name: String,
    val alamat: String,
    val area: String,
    val seed: String
)


class LahanAdapter(private val Lahans: List<Lahan>) :
    RecyclerView.Adapter<LahanAdapter.LahanViewHolder>() {

    class LahanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lahanImageView: ImageView = view.findViewById(R.id.item_lahan_gambar)
        val lahanNameTextView: TextView = view.findViewById(R.id.item_lahan_nama)
        val lahanAlamatTextView: TextView = view.findViewById(R.id.item_lahan_alamat)
        val lahanAreaTextView: TextView = view.findViewById(R.id.item_lahan_luas)
        val lahanSeedTextView: TextView = view.findViewById(R.id.item_lahan_bibit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LahanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lahan_item, parent, false)
        return LahanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LahanViewHolder, position: Int) {
        val lahan = Lahans[position]
        holder.lahanImageView.setImageResource(lahan.imageResId)
        holder.lahanNameTextView.text = lahan.name
        holder.lahanAlamatTextView.text = lahan.alamat
        holder.lahanAreaTextView.text =
            holder.itemView.context.getString(R.string.item_lahan_luas, lahan.area)
        holder.lahanSeedTextView.text =
            holder.itemView.context.getString(R.string.item_lahan_bibit, lahan.seed)
    }

    override fun getItemCount() = Lahans.size
}
