package com.ykstar.bangkit.taniland.pages.riwayattanam

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.adapters.RiwayatTanamAdapter
import com.ykstar.bangkit.taniland.databinding.ActivityRiwayatTanamBinding
import com.ykstar.bangkit.taniland.pages.premium.PlanPremiumActivity
import com.ykstar.bangkit.taniland.preferences.UserPreference
import com.ykstar.bangkit.taniland.utils.InternetActive
import com.ykstar.bangkit.taniland.utils.Resource
import com.ykstar.bangkit.taniland.utils.showPrimaryToast
import com.ykstar.bangkit.taniland.viewmodels.TanamViewModel

class RiwayatTanamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRiwayatTanamBinding
    private lateinit var viewModel: TanamViewModel
    private lateinit var userPreferences: UserPreference
    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatTanamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreference(this)
        viewModel = ViewModelProvider(this)[TanamViewModel::class.java]

        binding.backButton.setOnClickListener {
            finish()
        }

        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val lahanId = intent.getStringExtra(LAHAN_ID).toString()

        if (!InternetActive.isOnline(this)) {
            InternetActive.showNoInternetDialog(this)
        } else {
            progressDialog.show()

            getAllRiwayatTanam(lahanId)

        }
    }

    private fun getAllRiwayatTanam(lahanId: String) {
        val token = userPreferences.getToken().toString()
        viewModel.getRiwayatTanam(token, lahanId)
        viewModel.riwayatTanamResponse.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { riwayatTanamResponse ->
                        binding.noData.visibility =
                            if (riwayatTanamResponse.data.isEmpty()) View.VISIBLE else View.GONE
                        binding.riwayatTanamRecyclerView.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter =
                                RiwayatTanamAdapter(riwayatTanamResponse.data) {
                                    val intent = Intent(
                                        this@RiwayatTanamActivity,
                                        PlanPremiumActivity::class.java
                                    )
                                    startActivity(intent)
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

    companion object {
        private const val LAHAN_ID = "lahan_id"
    }

}
