package com.ykstar.bangkit.taniland.pages.homepage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.adapters.Lahan
import com.ykstar.bangkit.taniland.adapters.LahanAdapter
import com.ykstar.bangkit.taniland.databinding.ActivityHomeBinding
import com.ykstar.bangkit.taniland.utils.InternetActive
import com.ykstar.bangkit.taniland.utils.clearStatusBar

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
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

        val recyclerView: RecyclerView = binding.lahanHomeRv
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LahanAdapter(lands)
    }

}