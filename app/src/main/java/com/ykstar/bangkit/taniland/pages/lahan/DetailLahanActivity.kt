package com.ykstar.bangkit.taniland.pages.lahan

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.databinding.ActivityDetailLahanBinding
import com.ykstar.bangkit.taniland.databinding.PlanTanamDetailLahanBinding
import com.ykstar.bangkit.taniland.databinding.PopupRekomendasiTanamBinding
import com.ykstar.bangkit.taniland.databinding.PreTanamDetailLahanBinding

class DetailLahanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLahanBinding
    private lateinit var dialogBinding: PopupRekomendasiTanamBinding
    private lateinit var preTanamDetailLahanBinding: PreTanamDetailLahanBinding
    private lateinit var planTanamDetailLahanBinding: PlanTanamDetailLahanBinding
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLahanBinding.inflate(layoutInflater)
        dialogBinding = PopupRekomendasiTanamBinding.inflate(layoutInflater)
        preTanamDetailLahanBinding = PreTanamDetailLahanBinding.inflate(layoutInflater)
        planTanamDetailLahanBinding = PlanTanamDetailLahanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi dialog
        dialog = Dialog(this)
        dialog.setContentView(dialogBinding.root)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


        // Set onClickListener untuk tombol rekomendasi tanam
        preTanamDetailLahanBinding.buttonRekomendasiTanam.setOnClickListener {
            showDialog()
        }

        // Set aksi untuk tombol atau gambar di dalam dialog
        dialogBinding.menuKamera.setOnClickListener {
            // Aksi ketika tombol atau gambar kamera diklik
        }
        dialogBinding.menuGaleri.setOnClickListener {
            // Aksi ketika tombol atau gambar galeri diklik
        }
        dialogBinding.menuIot.setOnClickListener {
            // Aksi ketika tombol atau gambar IoT diklik
        }
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun showDialog() {
        dialog.show()
    }
}