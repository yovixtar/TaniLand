package com.ykstar.bangkit.taniland.pages.aktivitas

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.databinding.ActivityTambahAktivitasBinding
import com.ykstar.bangkit.taniland.utils.showPrimaryToast


class TambahAktivitasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahAktivitasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTambahAktivitasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.buttonSimpan.setOnClickListener {
            showPrimaryToast(getString(R.string.coming_soon))
        }

        val adapterKategoriAktivitas = ArrayAdapter.createFromResource(
            this,
            R.array.kategori_aktivitas, android.R.layout.simple_spinner_item
        )

        adapterKategoriAktivitas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.inputKategori.adapter = adapterKategoriAktivitas

        binding.inputKategori.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selected = parent.getItemAtPosition(position).toString()

                if (selected == "Pemupukan") {
                    binding.layoutPupuk.visibility = View.VISIBLE
                } else {
                    binding.layoutPupuk.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val adapterJenisBibit = ArrayAdapter.createFromResource(
            this,
            R.array.jenis_pupuk, android.R.layout.simple_spinner_item
        )

        adapterJenisBibit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.inputJenisPupuk.adapter = adapterJenisBibit
    }
}