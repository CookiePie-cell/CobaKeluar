package com.salugan.cobakeluar.model

data class HasilModel(
    var id: String?,
    val userId: String?,
    val nilai: String?,
    val tanggal: String?,
    val waktu: String?,
    val benar: Int?,
    val salah: Int?,
    val kosong: Int?
)
