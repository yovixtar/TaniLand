package com.ykstar.bangkit.taniland.models

data class TanamModel(
    val id: String,
    val jarak: Int,
    val status: String,
    val tanggal_tanam: String,
    val tanggal_panen: String,
    val jumlah_panen: Int,
    val harga_panen: Int,
    val umur: Int,
    val bibit: BibitModel,
    val aktivitas: List<AktivitasModel>?
)