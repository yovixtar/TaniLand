package com.ykstar.bangkit.taniland.pages

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.databinding.ActivityMainBinding
import com.ykstar.bangkit.taniland.pages.lahan.TambahLahanActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(binding.navHostFragment)
        navView.setupWithNavController(navController)

        val backButton: ImageButton = binding.backButton
        backButton.setOnClickListener {
            navController.navigate(R.id.navigation_home)
        }

        val toolbar: Toolbar = binding.toolbar
        val btn_tambah: FloatingActionButton = binding.btnMenuTambah
        val title: TextView = binding.mainTitle
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    toolbar.visibility = View.GONE
                    btn_tambah.visibility = View.GONE
                }

                R.id.navigation_lahan -> {
                    toolbar.visibility = View.VISIBLE
                    btn_tambah.visibility = View.VISIBLE
                    title.text = getString(R.string.lahan)
                }

                R.id.navigation_artikel -> {
                    toolbar.visibility = View.VISIBLE
                    btn_tambah.visibility = View.GONE
                    title.text = getString(R.string.artikel)
                }

                R.id.navigation_profile -> {
                    toolbar.visibility = View.VISIBLE
                    btn_tambah.visibility = View.GONE
                    title.text = getString(R.string.profile)
                }

                else -> {
                    toolbar.visibility = View.GONE
                    btn_tambah.visibility = View.GONE
                }
            }
        }

//        if (intent.getStringExtra("menu") == "menuLahan") {
//            navController.navigate(R.id.navigation_lahan)
//        }

        binding.btnMenuTambah.setOnClickListener {
            val intent = Intent(this, TambahLahanActivity::class.java)
            startActivity(intent)
        }
    }
}