package com.ykstar.bangkit.taniland.utils

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.ykstar.bangkit.taniland.R

object InternetActive {

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            networkInfo != null && networkInfo.isConnected
        }
    }

    fun showNoInternetDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.tidak_ada_koneksi_internet))
            .setMessage(context.getString(R.string.mohon_hubungkan_kembali_koneksi_internet_anda_dan_coba_kembali))
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}

sealed class Resource<T>(
    val data: T? = null,
    val exception: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(exception: Throwable, data: T? = null) : Resource<T>(data, exception)
}