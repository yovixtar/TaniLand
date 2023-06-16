package com.ykstar.bangkit.taniland.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.ykstar.bangkit.taniland.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID"))

    val date: Date = inputFormat.parse(dateString) as Date
    return outputFormat.format(date)
}

fun formatDateToAPI(dateString: String): String {
    val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID"))
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val date: Date = inputFormat.parse(dateString) as Date
    return outputFormat.format(date)
}

@Suppress("DEPRECATION")
@SuppressLint("InflateParams")
fun Context.showPrimaryToast(message: String, primary: Boolean = true) {
    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val layout: View = if (primary) {
        inflater.inflate(R.layout.primary_toast, null)
    } else {
        inflater.inflate(R.layout.danger_toast, null)
    }

    val image = layout.findViewById<ImageView>(R.id.custom_toast_image)
    image.setImageResource(R.drawable.ic_taniland_green)

    val text = layout.findViewById<TextView>(R.id.custom_toast_message)
    text.text = message

    with(Toast(applicationContext)) {
        duration = Toast.LENGTH_LONG
        view = layout
        show()
    }
}

