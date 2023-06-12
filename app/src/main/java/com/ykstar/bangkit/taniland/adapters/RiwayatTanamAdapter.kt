package com.ykstar.bangkit.taniland.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.models.RiwayatTanamList
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RiwayatTanamAdapter(
    private val riwayatTanamList: List<RiwayatTanamList>,
    private val onLihatLaporanClickListener: (RiwayatTanamList) -> Unit
) :
    RecyclerView.Adapter<RiwayatTanamAdapter.RiwayatTanamViewHolder>() {

    class RiwayatTanamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_riwayat_tanam_gambar)
        val nameTextView: TextView = itemView.findViewById(R.id.item_riwayat_tanam_nama)
        val tanggalPanenTextView: TextView =
            itemView.findViewById(R.id.item_riwayat_tanam_tanggal_panen)
        val jumlahPanenTextView: TextView =
            itemView.findViewById(R.id.item_riwayat_tanam_jumlah_panen)
        val lihatLaporanButton: Button =
            itemView.findViewById(R.id.item_riwayat_tanam_lihat_laporan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatTanamViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.riwayat_tanam_item, parent, false)
        return RiwayatTanamViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatTanamViewHolder, position: Int) {
        val riwayatTanam = riwayatTanamList[position]

//        Glide.with(holder.itemView.context)
//            .load(riwayatTanam.photo_bibit)
//            .into(holder.imageView)

        holder.nameTextView.text = riwayatTanam.bibit_nama

        holder.tanggalPanenTextView.text = formatDate(riwayatTanam.tanggal_panen)

        holder.jumlahPanenTextView.text = riwayatTanam.jumlah_panen.toString()

        holder.lihatLaporanButton.setOnClickListener {
            onLihatLaporanClickListener(riwayatTanam)
        }
    }

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID"))

        val date: Date = inputFormat.parse(dateString) as Date
        return outputFormat.format(date)
    }

    override fun getItemCount() = riwayatTanamList.size
}
