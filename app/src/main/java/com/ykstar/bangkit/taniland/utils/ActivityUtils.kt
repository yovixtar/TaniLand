package com.ykstar.bangkit.taniland.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.ykstar.bangkit.taniland.R

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

fun Context.showPrimaryToast(message: String, primary: Boolean = true) {
    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val layout: View = if(primary){
        inflater.inflate(R.layout.primary_toast, null)
    } else {
        inflater.inflate(R.layout.danger_toast, null)
    }

    val image = layout.findViewById<ImageView>(R.id.custom_toast_image)
    image.setImageResource(R.drawable.ic_taniland_green)

    val text = layout.findViewById<TextView>(R.id.custom_toast_message)
    text.text = message

    with (Toast(applicationContext)) {
        duration = Toast.LENGTH_LONG
        view = layout
        show()
    }
}

