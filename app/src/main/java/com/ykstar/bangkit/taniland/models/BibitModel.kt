package com.ykstar.bangkit.taniland.models

data class BibitModel(
    val id: String,
    val nama: String,
    val photo: String,
    val deskripsi: String,
    val harga_beli: Int,
    val jenis: String,
    val link_market: String
)

data class BibitResponse(
    val error: Boolean,
    val message: String,
    val data: List<BibitModel>
)