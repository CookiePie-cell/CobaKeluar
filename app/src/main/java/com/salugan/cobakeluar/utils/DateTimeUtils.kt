package com.salugan.cobakeluar.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtils {
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd:MM:yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}