package com.salugan.cobakeluar.data.remote.retrofit.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectionItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("id_selection")
    val idSelection: Int? = null,

    @field:SerializedName("selection_text")
    val selectionText: String? = null,

    @field:SerializedName("question_id")
    val questionId: Int? = null
) : Parcelable