package com.ykstar.bangkit.taniland.pages.camera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.databinding.ActivityScanQrBinding
import com.ykstar.bangkit.taniland.preferences.IoTPreference
import com.ykstar.bangkit.taniland.utils.showPrimaryToast

class ScanQRActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanQrBinding
    private  lateinit var ioTPreference: IoTPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ioTPreference = IoTPreference(this)

        val lahanId = intent.getStringExtra("lahan_id").toString()

        binding.barcodeView.decodeSingle(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                if (ioTPreference.saveIot(lahanId, result.text)){
                    val intent = Intent()
                    intent.putExtra("SCAN_IOT_ID", result.text)
                    setResult(Activity.RESULT_OK, intent)
                    showPrimaryToast(getString(R.string.berhasil_menghubungkan_iot_ke_lahan))
                    finish()
                }else {
                    showPrimaryToast(getString(R.string.iot_tool_sudah_terdaftar_pada_lahan_anda), false)
                    recreate()
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
}