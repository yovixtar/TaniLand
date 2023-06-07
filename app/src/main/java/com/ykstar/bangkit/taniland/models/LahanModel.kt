package com.ykstar.bangkit.taniland.models

data class LahanRequest(
    val user_id: String,
    val nama: String,
    val luas: Double,
    val alamat: String,
    val lat: Double?,
    val lon: Double?
)

data class LahanResponse(
    val data: List<LahanModel>,
    val error: Boolean,
    val message: String
)

data class LahanModel(
    val id: String,
    val user_id: String,
    val nama: String,
    val photo: String,
    val luas: Double,
    val alamat: String,
    val lat: Double?,
    val lon: Double?
)