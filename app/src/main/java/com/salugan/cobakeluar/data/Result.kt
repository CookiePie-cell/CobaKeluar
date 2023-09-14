package com.salugan.cobakeluar.data

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out E>(val errorData: E) : Result<Nothing>()
    object Loading : Result<Nothing>()
}