package com.salugan.cobakeluar.model

data class UserModel(
    val id: String?,
    val userId: String?,
    val nama: String?,
    val email: String?,
    val noHp: String?,

    ){
    constructor() : this("","", "", "", "")

}