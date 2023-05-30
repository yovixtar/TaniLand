package com.ykstar.bangkit.taniland.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View

fun Activity.clearStatusBar() {
    window.apply {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarColor = Color.TRANSPARENT
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}