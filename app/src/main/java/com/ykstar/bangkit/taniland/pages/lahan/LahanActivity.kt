package com.ykstar.bangkit.taniland.pages.lahan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.adapters.Lahan
import com.ykstar.bangkit.taniland.adapters.LahanAdapter
import com.ykstar.bangkit.taniland.databinding.ActivityLahanBinding
import com.ykstar.bangkit.taniland.utils.InternetActive
import com.ykstar.bangkit.taniland.utils.clearStatusBar

class LahanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLahanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLahanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!InternetActive.isOnline(this)) {
            InternetActive.showNoInternetDialog(this)
        }

        val lands = listOf(
            Lahan(R.drawable.bibit, "Lahan 1", "Desa Cangak, Kecamatan Bodeh", "8", "Jagung"),
            Lahan(R.drawable.bibit, "Lahan 2", "Desa Cangak, Kecamatan Bodeh", "10", "Padi"),
            Lahan(R.drawable.bibit, "Lahan 2", "Desa Cangak, Kecamatan Bodeh", "10", "Padi"),
            Lahan(R.drawable.bibit, "Lahan 2", "Desa Cangak, Kecamatan Bodeh", "10", "Padi"),
            Lahan(R.drawable.bibit, "Lahan 2", "Desa Cangak, Kecamatan Bodeh", "10", "Padi"),
            Lahan(R.drawable.bibit, "Lahan 2", "Desa Cangak, Kecamatan Bodeh", "10", "Padi"),
        )

        val recyclerView: RecyclerView = findViewById(R.id.lahanRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LahanAdapter(lands)
    }
}