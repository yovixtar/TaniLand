package com.ykstar.bangkit.taniland.models

data class TanamModel(
    val id: String,
    val jarak: Int,
    val status: String,
    val tanggal_tanam: String?,
    val tanggal_panen: String?,
    val jumlah_panen: Int?,
    val harga_panen: Int?,
    val umur: String,
    val bibit: BibitModel,
    val aktivitas: List<AktivitasModel>?
)

data class StatusTanamPlanRequest(
    val bibit_id: String,
    val lahan_id: String
)

data class StatusTanamExecRequest(
    val id: String?,
    val jarak: Int?,
    val tanggal_tanam: String?
)

data class StatusTanamCloseRequest(
    val id: String?,
    val tanggal_panen: String?,
    val jumlah_panen: Int?,
    val harga_panen: Int?,
)

data class RiwayatTanamResponse(
    val error: Boolean,
    val message: String,
    val data: List<RiwayatTanamList>
)

data class RiwayatTanamList(
    val id: String,
    val bibit_nama: String,
    val jarak: Int,
    val status: String,
    val tanggal_tanam: String,
    val tanggal_panen: String,
    val jumlah_panen: Int,
    val harga_panen: Int
)
