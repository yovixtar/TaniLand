package com.ykstar.bangkit.taniland.pages.premium

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ykstar.bangkit.taniland.BuildConfig
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.databinding.ActivityPlanPremiumBinding
import com.ykstar.bangkit.taniland.utils.showPrimaryToast

class PlanPremiumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanPremiumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.closeButton.setOnClickListener {
            finish()
        }

        binding.contactSalesButton.setOnClickListener {
            val phone = ""
            val message = "Halo, saya tertarik untuk meng-upgrade ke TaniLand Pro."

            val isWhatsappInstalled = isAppInstalled(this, WA_PKG)
            if (isWhatsappInstalled) {
                val sendIntent = Intent(INTENT_MAIN)
                sendIntent.action = Intent.ACTION_VIEW
                sendIntent.setPackage(WA_PKG)
                val url = BuildConfig.WA_API_URL
                sendIntent.data = Uri.parse(url)
                startActivity(sendIntent)
            } else {
                showPrimaryToast(getString(R.string.whatsapp_not_installed), false)
                val uri = Uri.parse(MARKET_WA)
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                startActivity(goToMarket)
            }
        }

    }

    @Suppress("DEPRECATION")
    private fun isAppInstalled(context: Context, packageName: String): Boolean {
        val packageManager = context.packageManager
        return try {
            packageManager.getPackageArchiveInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        private const val WA_PKG = "com.whatsapp"
        private const val INTENT_MAIN = "android.intent.action.MAIN"
        private const val MARKET_WA = "market://details?id=com.whatsapp"
    }
}

