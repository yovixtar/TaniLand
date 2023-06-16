package com.ykstar.bangkit.taniland.pages.lahan

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.databinding.ActivityDetailLahanBinding
import com.ykstar.bangkit.taniland.databinding.IotSectionDetailLahanBinding
import com.ykstar.bangkit.taniland.databinding.PlanTanamDetailLahanBinding
import com.ykstar.bangkit.taniland.databinding.PopupFormStatusTanamCloseBinding
import com.ykstar.bangkit.taniland.databinding.PopupFormStatusTanamExecBinding
import com.ykstar.bangkit.taniland.databinding.PopupRekomendasiTanamBinding
import com.ykstar.bangkit.taniland.databinding.PreTanamDetailLahanBinding
import com.ykstar.bangkit.taniland.models.DetailLahanResponse
import com.ykstar.bangkit.taniland.models.LahanDetail
import com.ykstar.bangkit.taniland.pages.MainActivity
import com.ykstar.bangkit.taniland.pages.aktivitas.TambahAktivitasActivity
import com.ykstar.bangkit.taniland.pages.bibit.BibitActivity
import com.ykstar.bangkit.taniland.pages.camera.ScanQRActivity
import com.ykstar.bangkit.taniland.pages.premium.PlanPremiumActivity
import com.ykstar.bangkit.taniland.pages.riwayattanam.RiwayatTanamActivity
import com.ykstar.bangkit.taniland.preferences.IoTPreference
import com.ykstar.bangkit.taniland.preferences.UserPreference
import com.ykstar.bangkit.taniland.utils.InternetActive
import com.ykstar.bangkit.taniland.utils.Resource
import com.ykstar.bangkit.taniland.utils.formatDate
import com.ykstar.bangkit.taniland.utils.formatDateToAPI
import com.ykstar.bangkit.taniland.utils.showPrimaryToast
import com.ykstar.bangkit.taniland.viewmodels.IoTViewModel
import com.ykstar.bangkit.taniland.viewmodels.LahanViewModel
import com.ykstar.bangkit.taniland.viewmodels.TanamViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailLahanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLahanBinding
    private lateinit var rekomendasiDialogBinding: PopupRekomendasiTanamBinding
    private lateinit var formExecDialogBinding: PopupFormStatusTanamExecBinding
    private lateinit var formCloseDialogBinding: PopupFormStatusTanamCloseBinding
    private lateinit var iotSectionDetailLahanBinding: IotSectionDetailLahanBinding
    private lateinit var preTanamDetailLahanBinding: PreTanamDetailLahanBinding
    private lateinit var planTanamDetailLahanBinding: PlanTanamDetailLahanBinding

    private lateinit var rekomendasiDialog: Dialog
    private lateinit var formExecDialog: Dialog
    private lateinit var formCloseDialog: Dialog

    private lateinit var progressDialog: Dialog

    private val lahanViewModel: LahanViewModel by viewModels()
    private val tanamViewModel: TanamViewModel by viewModels()
    private val iotViewModel: IoTViewModel by viewModels()

    private lateinit var userPreferences: UserPreference
    private lateinit var ioTPreference: IoTPreference

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLahanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreference(this)
        ioTPreference = IoTPreference(this)

        binding.backButton.setOnClickListener {
            finish()
        }

        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    recreate()
                }
            }

        val iotSectionDetailLahanLayout = findViewById<ViewGroup>(R.id.iot_section_detail_lahan)
        val preTanamDetailLahanLayout = findViewById<ViewGroup>(R.id.pre_tanam_detail_lahan)
        val planTanamDetailLahanLayout = findViewById<ViewGroup>(R.id.plan_tanam_detail_lahan)

        iotSectionDetailLahanBinding =
            IotSectionDetailLahanBinding.bind(iotSectionDetailLahanLayout)
        preTanamDetailLahanBinding = PreTanamDetailLahanBinding.bind(preTanamDetailLahanLayout)
        planTanamDetailLahanBinding = PlanTanamDetailLahanBinding.bind(planTanamDetailLahanLayout)

        val lahanId = intent.getStringExtra(LAHAN_ID).toString()

        val token = userPreferences.getToken().toString()

        if (!InternetActive.isOnline(this)) {
            InternetActive.showNoInternetDialog(this)
        } else {
            lahanViewModel.getLahanDetail(lahanId, token)

            lahanViewModel.lahanState.observe(this) { state ->
                when (state) {
                    is LahanViewModel.LahanState.Loading -> {
                        progressDialog.show()
                    }

                    is LahanViewModel.LahanState.Success<*> -> {
                        when (val resource = state.data) {
                            is Resource.Success<*> -> {
                                if (resource.data is DetailLahanResponse) {

                                    val detailLahanResponse = resource.data.data
                                    updateUI(token, detailLahanResponse, progressDialog)

                                    val iotID = ioTPreference.getIot(detailLahanResponse.id)
                                    if (iotID?.isNotEmpty() == true) {
                                        updateUI_IoTSection(iotID, token, lahanId)
                                    } else {
                                        updateUI_HubungkanIoT(lahanId)
                                    }
                                    onClickPilihBibit(detailLahanResponse.id)
                                }
                                progressDialog.dismiss()
                            }

                            is Resource.Error<*> -> {
                                showPrimaryToast(getString(R.string.kesalahan_memuat_data), false)
                                progressDialog.dismiss()
                                recreate()
                            }
                        }
                    }

                    is LahanViewModel.LahanState.Error -> {
                        progressDialog.dismiss()
                    }
                }
            }
        }
    }

    private fun onClickPilihBibit(lahan_id: String) {
        preTanamDetailLahanBinding.buttonPilihBibit.setOnClickListener {
            val intent = Intent(this, BibitActivity::class.java)
            intent.putExtra(LAHAN_ID, lahan_id)
            startActivity(intent)
        }
    }

    private fun updateUI(token: String, lahanDetail: LahanDetail, progressBar: Dialog) {
        val lahanId = lahanDetail.id
        val tanam = lahanDetail.tanam
        val status = lahanDetail.tanam?.status

        binding.deleteLahan.setOnClickListener()
        {
            deleteLahanAction(token, lahanId, progressBar)
        }
        binding.lahanItem.itemLahanNama.text = lahanDetail.nama
        binding.lahanItem.itemLahanAlamat.text = lahanDetail.alamat
        binding.lahanItem.itemLahanLuas.text =
            getString(R.string.item_lahan_luas, lahanDetail.luas.toString())

        Glide.with(this)
            .load(lahanDetail.photo)
            .into(binding.lahanItem.itemLahanGambar)

        binding.preTanamDetailLahan.root.visibility = View.GONE
        binding.planTanamDetailLahan.root.visibility = View.GONE
        binding.execTanamDetailLahan.root.visibility = View.GONE

        if (tanam == null) {
            updateUI_StatusTanamPre(lahanId)
        } else {
            when (status) {
                STATUS_PLAN -> updateUI_StatusTanamPlan(token, lahanDetail, progressBar)
                STATUS_EXEC -> updateUI_StatusTanamExec(token, lahanDetail, progressBar)
                else -> updateUI_StatusTanamPre(lahanId)
            }
        }
    }

    private fun updateUI_HubungkanIoT(lahanId: String) {
        binding.iotSectionDetailLahan.buttonHubungkanIot.visibility =
            View.VISIBLE
        iotSectionDetailLahanBinding.buttonHubungkanIot.setOnClickListener {
            val intent = Intent(this, ScanQRActivity::class.java)
            intent.putExtra(LAHAN_ID, lahanId)
            resultLauncher.launch(intent)
        }
    }

    private fun updateUI_IoTSection(iotId: String, token: String?, lahanId: String) {
        binding.iotSectionDetailLahan.buttonHubungkanIot.visibility = View.GONE
        binding.iotSectionDetailLahan.infoCard.visibility = View.VISIBLE

        actionGetHasilIoT(iotId, token)

        binding.iotSectionDetailLahan.buttonReload.setOnClickListener {
            actionGetHasilIoT(iotId, token)
            recreate()
        }

        binding.iotSectionDetailLahan.buttonDelete.setOnClickListener {
            actionResetIoT(token, iotId, lahanId)
        }
    }

    private fun updateUI_StatusTanamPre(lahanId: String) {
        binding.preTanamDetailLahan.root.visibility = View.VISIBLE

        rekomendasiDialog = Dialog(this).apply {
            setContentView(R.layout.popup_rekomendasi_tanam)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        rekomendasiDialogBinding =
            PopupRekomendasiTanamBinding.bind(rekomendasiDialog.findViewById(R.id.popup_rekomendasi_tanam))

        binding.preTanamDetailLahan.buttonRekomendasiTanam.setOnClickListener {
            rekomendasiDialog.show()
        }

        rekomendasiDialogBinding.menuKamera.setOnClickListener {
        }
        rekomendasiDialogBinding.menuGaleri.setOnClickListener {
        }
        rekomendasiDialogBinding.menuIot.setOnClickListener {
        }
        rekomendasiDialogBinding.btnCancel.setOnClickListener {
            rekomendasiDialog.dismiss()
        }

        binding.preTanamDetailLahan.buttonRiwayatTanam.setOnClickListener {
            Intent(this, RiwayatTanamActivity::class.java).also {
                it.putExtra(LAHAN_ID, lahanId)
                startActivity(it)
                finish()
            }
        }
    }

    private fun updateUI_StatusTanamPlan(
        token: String,
        lahanDetail: LahanDetail,
        progressBar: Dialog
    ) {
        binding.planTanamDetailLahan.root.visibility = View.VISIBLE

        val jenis = lahanDetail.tanam?.bibit?.jenis
        binding.lahanItem.itemLahanUmur.visibility = View.VISIBLE
        binding.lahanItem.itemLahanUmur.text = getString(R.string.jenis_detail, jenis)

        val nama = lahanDetail.tanam?.bibit?.nama
        binding.planTanamDetailLahan.itemBibitNama.text = nama

        val gambar = lahanDetail.tanam?.bibit?.photo
        Glide.with(this)
            .load(gambar)
            .into(binding.planTanamDetailLahan.itemBibitGambar)

        binding.planTanamDetailLahan.buttonDelete.setOnClickListener {
            deleteTanamAction(token, lahanDetail.tanam?.id.toString(), progressBar)
        }

        binding.planTanamDetailLahan.buttonTanamSekarang.setOnClickListener {
            actionButtonTanamSekarang(
                token = token,
                tanamId = lahanDetail.tanam?.id,
                progressBar = progressBar
            )
        }

        binding.planTanamDetailLahan.buttonBeli.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(lahanDetail.tanam?.bibit?.link_market)
            startActivity(intent)
        }

        binding.planTanamDetailLahan.buttonPurchaseOrder.setOnClickListener {
            val intent = Intent(this, PlanPremiumActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateUI_StatusTanamExec(
        token: String,
        lahanDetail: LahanDetail,
        progressBar: Dialog
    ) {
        binding.execTanamDetailLahan.root.visibility = View.VISIBLE

        val namaBibit = lahanDetail.tanam?.bibit?.nama
        binding.execTanamDetailLahan.namaBibit.text = namaBibit

        val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("in", "ID"))
        val tanggalTanam = lahanDetail.tanam?.tanggal_tanam

        try {
            val date = tanggalTanam?.let { inputFormat.parse(it) }
            binding.execTanamDetailLahan.tanggalTanam.text = date?.let { outputFormat.format(it) }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val jenisBibit = lahanDetail.tanam?.bibit?.jenis
        binding.execTanamDetailLahan.jenisBibit.text = jenisBibit

        val umurTanaman = lahanDetail.tanam?.umur
        binding.execTanamDetailLahan.umurTanam.text = getString(R.string.satuan_hari, umurTanaman)

        binding.execTanamDetailLahan.buttonKelolaKeuangan.setOnClickListener {
            val intent = Intent(this, PlanPremiumActivity::class.java)
            startActivity(intent)
        }

        binding.execTanamDetailLahan.buttonPanen.setOnClickListener {
            actionButtonPanen(
                token = token,
                tanamId = lahanDetail.tanam?.id,
                progressBar = progressBar
            )
        }

        binding.execTanamDetailLahan.buttonTambahAktivitas.setOnClickListener {
            val intent = Intent(this, TambahAktivitasActivity::class.java)
            startActivity(intent)
        }
    }

    private fun actionGetHasilIoT(iotId: String, token: String?) {
        iotViewModel.getHasilIoT(
            iotId = iotId,
            token = token,
        )

        iotViewModel.hasilIoTResponse.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val suhu = resource.data?.data?.suhu
                    val kelembabanUdara = resource.data?.data?.kelembaban_udara

                    binding.iotSectionDetailLahan.suhuValue.text =
                        getString(R.string.suhu_celcius, suhu.toString())
                    binding.iotSectionDetailLahan.kelembapanValue.text =
                        getString(R.string.kelembapan_persen, kelembabanUdara.toString())
                }

                is Resource.Error -> {
                    showPrimaryToast(
                        getString(R.string.kesalahan_memuat_data),
                        false
                    )
                    recreate()
                }
            }
        }
    }

    private fun actionResetIoT(token: String?, iotId: String, lahanId: String) {
        iotViewModel.resetIoT(
            token = token,
            iotId = iotId,
        )

        iotViewModel.iotRegResResponse.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    showPrimaryToast(
                        getString(R.string.data_iot_berhasil_terhapus)
                    )
                    ioTPreference.deleteIot(lahanId)
                    recreate()
                }

                is Resource.Error -> {
                    showPrimaryToast(
                        getString(R.string.kesalahan_memuat_data),
                        false
                    )
                }
            }
        }
    }

    private fun deleteLahanAction(token: String, lahan_id: String, progressBar: Dialog) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.hapus_lahan))
            .setMessage(getString(R.string.apakah_anda_yakin_ingin_menghapus_lahan_ini))
            .setPositiveButton(getString(R.string.ya)) { _, _ ->
                lahanViewModel.deleteLahan(token, lahan_id)

                lahanViewModel.lahanState.observe(this) { state ->
                    when (state) {
                        is LahanViewModel.LahanState.Loading -> {
                            progressBar.show()
                        }

                        is LahanViewModel.LahanState.Success<*> -> {
                            when (state.data) {
                                is Resource.Success<*> -> {
                                    progressBar.dismiss()
                                    showPrimaryToast(getString(R.string.berhasil_menghapus_lahan))
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.putExtra("menu", "menuLahan")
                                    startActivity(intent)
                                    finish()
                                }

                                is Resource.Error<*> -> {
                                    progressBar.dismiss()
                                    showPrimaryToast(
                                        getString(R.string.gagal_menghapus_lahan),
                                        false
                                    )
                                }
                            }
                        }

                        else -> {
                            progressBar.dismiss()
                            recreate()
                        }
                    }
                }
            }
            .setNegativeButton(getString(R.string.tidak)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteTanamAction(token: String, tanam_id: String, progressBar: Dialog) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.hapus_tanam))
            .setMessage(getString(R.string.apakah_anda_yakin_ingin_menghapus_plan_tanam_ini))
            .setPositiveButton(getString(R.string.ya)) { _, _ ->
                tanamViewModel.deleteTanam(token, tanam_id)

                progressBar.show()

                tanamViewModel.deleteTanamResponse.observe(this) { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            progressBar.dismiss()
                            showPrimaryToast(getString(R.string.berhasil_menghapus_plan_tanam))
                            recreate()
                        }

                        is Resource.Error -> {
                            progressBar.dismiss()
                            showPrimaryToast(getString(R.string.gagal_menghapus_plan_tanam), false)
                        }
                    }
                }
            }
            .setNegativeButton(getString(R.string.tidak)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun actionButtonTanamSekarang(
        token: String,
        tanamId: String?,
        progressBar: Dialog
    ) {
        formExecDialog = Dialog(this).apply {
            setContentView(R.layout.popup_form_status_tanam_exec)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        formExecDialogBinding =
            PopupFormStatusTanamExecBinding.bind(formExecDialog.findViewById(R.id.popup_form_status_tanam_exec))

        formExecDialog.show()

        formExecDialogBinding.jarakInput.addTextChangedListener(textWatcherFormExec)
        formExecDialogBinding.tanggalTanamInput.addTextChangedListener(textWatcherFormExec)
        formExecDialogBinding.btnSimpan.isEnabled = false

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                val format = SimpleDateFormat(FORMAT_DATE, Locale.getDefault())
                val dateString = format.format(calendar.time)
                formExecDialogBinding.tanggalTanamInput.setText(formatDate(dateString))
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        formExecDialogBinding.tanggalTanamInput.setOnClickListener {
            datePickerDialog.show()
        }

        formExecDialogBinding.btnSimpan.setOnClickListener {
            progressBar.show()
            val jarak = formExecDialogBinding.jarakInput.text.toString().toIntOrNull()
            val tanggalTanam = formatDateToAPI(formExecDialogBinding.tanggalTanamInput.text.toString())

            tanamViewModel.statusTanamExec(
                token = token,
                id = tanamId,
                jarak = jarak,
                tanggal_tanam = tanggalTanam
            )

            tanamViewModel.statusTanamResponse.observe(this) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        progressBar.dismiss()
                        recreate()
                    }

                    is Resource.Error -> {
                        progressBar.dismiss()
                        showPrimaryToast(getString(R.string.gagal_memperbarui_status_tanam), false)
                        Log.d("Gagal", resource.exception?.message.toString())

                    }
                }
            }

            formExecDialog.dismiss()
        }
    }

    private fun actionButtonPanen(
        token: String,
        tanamId: String?,
        progressBar: Dialog
    ) {
        formCloseDialog = Dialog(this).apply {
            setContentView(R.layout.popup_form_status_tanam_close)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        formCloseDialogBinding =
            PopupFormStatusTanamCloseBinding.bind(formCloseDialog.findViewById(R.id.popup_form_status_tanam_close))

        formCloseDialog.show()

        formCloseDialogBinding.tanggalPanenInput.addTextChangedListener(textWatcherFormClose)
        formCloseDialogBinding.jumlahPanenInput.addTextChangedListener(textWatcherFormClose)
        formCloseDialogBinding.hargaPanenInput.addTextChangedListener(textWatcherFormClose)
        formCloseDialogBinding.btnSimpan.isEnabled = false

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                val format = SimpleDateFormat(FORMAT_DATE, Locale.getDefault())
                val dateString = format.format(calendar.time)
                formCloseDialogBinding.tanggalPanenInput.setText(formatDate(dateString))
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        formCloseDialogBinding.tanggalPanenInput.setOnClickListener {
            datePickerDialog.show()
        }

        formCloseDialogBinding.btnSimpan.setOnClickListener {
            progressBar.show()
            val jumlahPanen = formCloseDialogBinding.jumlahPanenInput.text.toString().toIntOrNull()
            val tanggalPanen = formatDateToAPI(formCloseDialogBinding.tanggalPanenInput.text.toString())
            val hargaPanen = formCloseDialogBinding.hargaPanenInput.text.toString().toIntOrNull()

            tanamViewModel.statusTanamClose(
                token = token,
                id = tanamId,
                jumlahPanen = jumlahPanen,
                tanggalPanen = tanggalPanen,
                hargaPanen = hargaPanen
            )

            tanamViewModel.statusTanamResponse.observe(this) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        progressBar.dismiss()
                        recreate()
                    }

                    is Resource.Error -> {
                        progressBar.dismiss()
                        showPrimaryToast(getString(R.string.gagal_memperbarui_status_tanam), false)
                    }
                }
            }

            formCloseDialog.dismiss()
        }
    }

    private val textWatcherFormExec = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            formExecDialogBinding.btnSimpan.isEnabled = validateForm()
        }
    }

    private fun validateForm(): Boolean {
        return formExecDialogBinding.jarakInput.text.toString().isNotEmpty() &&
                formExecDialogBinding.tanggalTanamInput.text.toString().isNotEmpty()
    }

    private val textWatcherFormClose = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            formCloseDialogBinding.btnSimpan.isEnabled = validateFormClose()
        }
    }

    private fun validateFormClose(): Boolean {
        return formCloseDialogBinding.tanggalPanenInput.text.toString().isNotEmpty() &&
                formCloseDialogBinding.jumlahPanenInput.text.toString().isNotEmpty() &&
                formCloseDialogBinding.hargaPanenInput.text.toString().isNotEmpty()
    }

    companion object {
        private const val LAHAN_ID = "lahan_id"
        private const val STATUS_PLAN = "plan"
        private const val STATUS_EXEC = "exec"
        private const val FORMAT_DATE = "yyyy-MM-dd"
    }
}