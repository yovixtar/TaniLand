package com.ykstar.bangkit.taniland.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ykstar.bangkit.taniland.R

data class Produk(
    val image: Int,
    val name: String,
    val harga: String,
    val kategori: String,
)


class ProdukAdapter(private val produks: List<Produk>) :
    RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder>() {

    class ProdukViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val produkImageView: ImageView = view.findViewById(R.id.item_produk_gambar)
        val produkNameTextView: TextView = view.findViewById(R.id.item_produk_nama)
        val produkHargaTextView: TextView = view.findViewById(R.id.item_produk_harga)
        val produkKategoriTextView: TextView = view.findViewById(R.id.item_produk_kategori)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.produk_item, parent, false)
        return ProdukViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdukViewHolder, position: Int) {
        val produk = produks[position]
        holder.produkImageView.setImageResource(produk.image)
        holder.produkNameTextView.text = produk.name
        holder.produkHargaTextView.text =
            holder.itemView.context.getString(R.string.item_produk_harga, produk.harga)
        holder.produkKategoriTextView.text = produk.kategori
    }

    override fun getItemCount() = produks.size
}