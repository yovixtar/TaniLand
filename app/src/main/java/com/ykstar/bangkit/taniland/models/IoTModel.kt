package com.ykstar.bangkit.taniland.models

data class RegistIoTRequest(
    val iot_id: String,
    val lahan_id: String,
)

data class HasilIoTResponse(
    val error: Boolean,
    val message: String,
    val data: HasilIoTData,
)

data class HasilIoTData(
    val suhu: Double,
    val kelembaban_udara: Double,
)