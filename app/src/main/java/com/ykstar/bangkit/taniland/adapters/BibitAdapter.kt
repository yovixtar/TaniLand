package com.ykstar.bangkit.taniland.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.models.BibitModel
import java.text.NumberFormat
import java.util.Locale

class BibitAdapter(private val bibitList: List<BibitModel>,
                   private val onPilihBibitClickListener: (BibitModel) -> Unit) :
    RecyclerView.Adapter<BibitAdapter.BibitViewHolder>() {

    class BibitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_bibit_gambar)
        val nameTextView: TextView = itemView.findViewById(R.id.item_bibit_nama)
        val priceTextView: TextView = itemView.findViewById(R.id.item_bibit_harga)
        val typeChip: Chip = itemView.findViewById(R.id.item_bibit_jenis)
        val selectButton: Button = itemView.findViewById(R.id.item_bibit_pilih)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bibit_item, parent, false)
        return BibitViewHolder(view)
    }

    override fun onBindViewHolder(holder: BibitViewHolder, position: Int) {
        val bibit = bibitList[position]

        Glide.with(holder.itemView.context)
            .load(bibit.photo)
            .into(holder.imageView)

        holder.nameTextView.text = bibit.nama
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        val hargaBibit = numberFormat.format(bibit.harga_beli)
        holder.priceTextView.text = holder.itemView.context.getString(R.string.item_bibit_harga, hargaBibit)
        holder.typeChip.text = bibit.jenis

        holder.selectButton.setOnClickListener {
            onPilihBibitClickListener(bibit)
        }
    }

    override fun getItemCount() = bibitList.size
}
