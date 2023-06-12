package com.ykstar.bangkit.taniland.pages.bibit

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.adapters.BibitAdapter
import com.ykstar.bangkit.taniland.databinding.ActivityBibitBinding
import com.ykstar.bangkit.taniland.pages.lahan.DetailLahanActivity
import com.ykstar.bangkit.taniland.preferences.UserPreference
import com.ykstar.bangkit.taniland.utils.InternetActive
import com.ykstar.bangkit.taniland.utils.Resource
import com.ykstar.bangkit.taniland.utils.showPrimaryToast
import com.ykstar.bangkit.taniland.viewmodels.BibitViewModel
import com.ykstar.bangkit.taniland.viewmodels.TanamViewModel

class BibitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBibitBinding
    private lateinit var viewModel: BibitViewModel
    private lateinit var tanamViewModel: TanamViewModel
    private lateinit var userPreferences: UserPreference
    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBibitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreference(this)
        viewModel = ViewModelProvider(this)[BibitViewModel::class.java]
        tanamViewModel = ViewModelProvider(this)[TanamViewModel::class.java]

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

            getAllBibit(lahan_id)

        }
    }

    private fun getAllBibit(lahan_id: String){
        val token = userPreferences.getToken().toString()
        viewModel.getAllBibit(token)
        viewModel.bibitResponse.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { bibitResponse ->
                        binding.noData.visibility =
                            if (bibitResponse.data.isEmpty()) View.VISIBLE else View.GONE
                        binding.bibitRecyclerView.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = BibitAdapter(bibitResponse.data) { bibit ->
                                pilihBibit(token, bibit.id, lahan_id)
                            }
                        }
                        progressDialog.dismiss()
                    }
                }

                is Resource.Error -> {
                    showPrimaryToast(getString(R.string.kesalahan_memuat_data), false)
                    progressDialog.dismiss()
                }
            }
        }
    }

    private fun pilihBibit(token: String, bibit_id: String, lahan_id: String){
        tanamViewModel.statusTanamPlan(
            token, bibit_id, lahan_id
        )

        tanamViewModel.statusTanamResponse.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    progressDialog.dismiss()
                    Intent(this, DetailLahanActivity::class.java).also {
                        it.putExtra("lahan_id", lahan_id)
                        startActivity(it)
                        finish()
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(this, resource.exception?.message, Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }
            }
        }
    }
}