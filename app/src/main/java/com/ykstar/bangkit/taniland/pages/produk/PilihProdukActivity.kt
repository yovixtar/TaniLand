package com.ykstar.bangkit.taniland.pages.produk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.adapters.Produk
import com.ykstar.bangkit.taniland.adapters.ProdukAdapter
import com.ykstar.bangkit.taniland.databinding.ActivityPilihProdukBinding
import com.ykstar.bangkit.taniland.utils.InternetActive
import com.ykstar.bangkit.taniland.utils.clearStatusBar

class PilihProdukActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPilihProdukBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!InternetActive.isOnline(this)) {
            InternetActive.showNoInternetDialog(this)
        }

        val produks = listOf(
            Produk(
                R.drawable.bibit,
                "Bibit Cabai Merah",
                "30.000",
                "Cabai",
            ),
            Produk(
                R.drawable.bibit,
                "Bibit Cabai Hijau",
                "25.000",
                "Cabai",
            ),
            Produk(
                R.drawable.bibit,
                "Bibit Terong Ungu",
                "28.000",
                "Terong",
            )
        )

        val recyclerView: RecyclerView = binding.produkRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ProdukAdapter(produks)
    }
}