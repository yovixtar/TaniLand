package com.ykstar.bangkit.taniland.pages.lahan

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.databinding.ActivityDetailLahanBinding
import com.ykstar.bangkit.taniland.databinding.PlanTanamDetailLahanBinding
import com.ykstar.bangkit.taniland.databinding.PopupRekomendasiTanamBinding
import com.ykstar.bangkit.taniland.databinding.PreTanamDetailLahanBinding
import com.ykstar.bangkit.taniland.models.LahanDetail
import com.ykstar.bangkit.taniland.pages.bibit.BibitActivity
import com.ykstar.bangkit.taniland.preferences.UserPreference
import com.ykstar.bangkit.taniland.utils.Resource
import com.ykstar.bangkit.taniland.viewmodels.LahanViewModel

class DetailLahanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLahanBinding
    private lateinit var dialogBinding: PopupRekomendasiTanamBinding
    private lateinit var preTanamDetailLahanBinding: PreTanamDetailLahanBinding
    private lateinit var planTanamDetailLahanBinding: PlanTanamDetailLahanBinding
    private lateinit var dialog: Dialog

    private val viewModel: LahanViewModel by viewModels()
    private lateinit var userPreferences: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLahanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreference(this)

        binding.backButton.setOnClickListener(){
            finish()
        }

        val preTanamDetailLahanLayout = findViewById<ViewGroup>(R.id.pre_tanam_detail_lahan)
        val planTanamDetailLahanLayout = findViewById<ViewGroup>(R.id.plan_tanam_detail_lahan)

        preTanamDetailLahanBinding = PreTanamDetailLahanBinding.bind(preTanamDetailLahanLayout)
        planTanamDetailLahanBinding = PlanTanamDetailLahanBinding.bind(planTanamDetailLahanLayout)

        dialog = Dialog(this).apply {
            setContentView(R.layout.popup_rekomendasi_tanam) // assuming the layout file is popup_rekomendasi_tanam.xml
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        dialogBinding = PopupRekomendasiTanamBinding.bind(dialog.findViewById(R.id.popup_rekomendasi_tanam))

        preTanamDetailLahanBinding.buttonRekomendasiTanam.setOnClickListener {
            showDialog()
        }

        dialogBinding.menuKamera.setOnClickListener {
        }
        dialogBinding.menuGaleri.setOnClickListener {
        }
        dialogBinding.menuIot.setOnClickListener {
        }
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }


        val id = intent.getStringExtra("id").toString()
        val token = userPreferences.getToken().toString()

        viewModel.getLahanDetail(id, token)

        viewModel.lahanDetail.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { detailLahanResponse ->
                        updateUI(detailLahanResponse.data)
                        onClickPilihBibit(detailLahanResponse.data.id)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(
                        this,
                        "Error Detail",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun showDialog() {
        dialog.show()
    }

    private fun onClickPilihBibit(lahan_id: String){
        preTanamDetailLahanBinding.buttonPilihBibit.setOnClickListener {
            val intent = Intent(this, BibitActivity::class.java)
            intent.putExtra("lahan_id", lahan_id)
            startActivity(intent)
        }
    }

    private fun updateUI(lahanDetail: LahanDetail) {
        val tanam = lahanDetail.tanam
        val status = lahanDetail.tanam?.status

        binding.lahanItem.itemLahanNama.text = lahanDetail.nama
        binding.lahanItem.itemLahanAlamat.text = lahanDetail.alamat
        binding.lahanItem.itemLahanLuas.text = getString(R.string.item_lahan_luas, lahanDetail.luas.toString())

        Glide.with(this)
            .load(lahanDetail.photo)
            .into(binding.lahanItem.itemLahanGambar)

        binding.preTanamDetailLahan.root.visibility = View.GONE
        binding.planTanamDetailLahan.root.visibility = View.GONE
        binding.execTanamDetailLahan.root.visibility = View.GONE

        if (tanam == null) {
            updateUI_StatusTanamPre()
        } else {
            when (status) {
                "plan" -> updateUI_StatusTanamPlan()
                "exec" -> updateUI_StatusTanamExec()
                else -> updateUI_StatusTanamPre()
            }
        }
    }

    private fun updateUI_StatusTanamPre(){
        binding.preTanamDetailLahan.root.visibility = View.VISIBLE
    }
    private fun updateUI_StatusTanamPlan(){
        binding.planTanamDetailLahan.root.visibility = View.VISIBLE
    }
    private fun updateUI_StatusTanamExec(){
        binding.execTanamDetailLahan.root.visibility = View.VISIBLE
    }

}