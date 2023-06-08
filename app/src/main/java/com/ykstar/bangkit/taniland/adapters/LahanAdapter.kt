package com.ykstar.bangkit.taniland.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.models.LahanModel
import com.ykstar.bangkit.taniland.pages.lahan.DetailLahanActivity

class LahanAdapter(private val lahanList: List<LahanModel>) :
    RecyclerView.Adapter<LahanAdapter.LahanViewHolder>() {

    class LahanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_lahan_gambar)
        val nameTextView: TextView = itemView.findViewById(R.id.item_lahan_nama)
        val addressTextView: TextView = itemView.findViewById(R.id.item_lahan_alamat)
        val areaTextView: TextView = itemView.findViewById(R.id.item_lahan_luas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LahanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lahan_item, parent, false)
        return LahanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LahanViewHolder, position: Int) {
        val lahan = lahanList[position]

        Glide.with(holder.itemView.context)
            .load(lahan.photo)
            .into(holder.imageView)

        holder.nameTextView.text = lahan.nama
        holder.addressTextView.text = lahan.alamat
        holder.areaTextView.text =
            holder.itemView.context.getString(R.string.item_lahan_luas, lahan.luas.toString())

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailLahanActivity::class.java)
            intent.putExtra("id", lahan.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = lahanList.size
}

