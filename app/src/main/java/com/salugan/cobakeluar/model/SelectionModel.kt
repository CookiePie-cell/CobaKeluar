package com.salugan.cobakeluar.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectionModel(
    val id: Int?,
    val questionId: Int?,
    val text: String?,
    val image: String?,
    var isSelected: Boolean,
) : Parcelable
