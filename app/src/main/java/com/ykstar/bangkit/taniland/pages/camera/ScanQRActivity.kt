package com.ykstar.bangkit.taniland.pages.camera

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.databinding.ActivityScanQrBinding
import com.ykstar.bangkit.taniland.preferences.IoTPreference
import com.ykstar.bangkit.taniland.preferences.UserPreference
import com.ykstar.bangkit.taniland.utils.Resource
import com.ykstar.bangkit.taniland.utils.showPrimaryToast
import com.ykstar.bangkit.taniland.viewmodels.IoTViewModel

class ScanQRActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanQrBinding
    private lateinit var ioTPreference: IoTPreference
    private lateinit var userPreference: UserPreference

    private val iotViewModel: IoTViewModel by viewModels()

    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        ioTPreference = IoTPreference(this)
        userPreference = UserPreference(this)

        val token = userPreference.getToken()
        val lahanId = intent.getStringExtra(LAHAN_ID).toString()

        binding.barcodeView.decodeSingle(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                progressDialog.show()
                iotViewModel.registIoT(
                    token = token,
                    iotId = result.text,
                    lahanId = lahanId,
                )

                iotViewModel.iotRegResResponse.observe(this@ScanQRActivity) { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            if (ioTPreference.saveIot(lahanId, result.text)) {
                                showPrimaryToast(getString(R.string.berhasil_menghubungkan_iot_ke_lahan))
                                progressDialog.dismiss()
                                finish()
                            }
                        }

                        is Resource.Error -> {
                            progressDialog.dismiss()
                            showPrimaryToast(
                                getString(R.string.iot_tool_sudah_terdaftar_pada_lahan_lain),
                                false
                            )
                        }
                    }
                }
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
        })
    }

    override fun onResume() {
        super.onResume()
        binding.barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.barcodeView.pause()
    }

    companion object {
        private const val LAHAN_ID = "lahan_id"
    }
}