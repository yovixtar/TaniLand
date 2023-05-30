package com.ykstar.bangkit.taniland.pages.startup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ykstar.bangkit.taniland.databinding.ActivityAuthBinding
import com.ykstar.bangkit.taniland.pages.homepage.HomeActivity
import com.ykstar.bangkit.taniland.utils.InternetActive

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!InternetActive.isOnline(this)) {
            InternetActive.showNoInternetDialog(this)
        }

        binding.btnGoogle.setOnClickListener {
            // Navigate to HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}