package com.ykstar.bangkit.taniland.pages.bibit

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.adapters.BibitAdapter
import com.ykstar.bangkit.taniland.adapters.LahanAdapter
import com.ykstar.bangkit.taniland.databinding.ActivityBibitBinding
import com.ykstar.bangkit.taniland.databinding.ActivityTambahLahanBinding
import com.ykstar.bangkit.taniland.databinding.FragmentLahanBinding
import com.ykstar.bangkit.taniland.preferences.UserPreference
import com.ykstar.bangkit.taniland.utils.InternetActive
import com.ykstar.bangkit.taniland.utils.Resource
import com.ykstar.bangkit.taniland.viewmodels.BibitViewModel
import com.ykstar.bangkit.taniland.viewmodels.LahanViewModel

class BibitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBibitBinding
    private lateinit var viewModel: BibitViewModel
    private lateinit var userPreferences: UserPreference
    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBibitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreference(this)
        viewModel = ViewModelProvider(this)[BibitViewModel::class.java]

        val lahan_id = intent.getStringExtra("lahan_id").toString()

        binding.backButton.setOnClickListener(){
            finish()
        }

        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (!InternetActive.isOnline(this)) {
            InternetActive.showNoInternetDialog(this)
        } else {
            progressDialog.show()
            userPreferences.getToken()?.let { viewModel.getAllBibit(it) }
            viewModel.bibitResponse.observe(this) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data?.let { bibitResponse ->
                            binding.noData.visibility =
                                if (bibitResponse.data.isEmpty()) View.VISIBLE else View.GONE
                            binding.bibitRecyclerView.apply {
                                layoutManager = LinearLayoutManager(context)
                                adapter = BibitAdapter(bibitResponse.data)
                            }
                            progressDialog.dismiss()
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(this, getString(R.string.kesalahan_memuat_data), Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                    }
                }
            }
        }
    }
}