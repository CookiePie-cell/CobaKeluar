package com.salugan.cobakeluar.data.remote.retrofit.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataItem(

    @field:SerializedName("tryout")
    val tryout: List<TryoutItem>,

    @field:SerializedName("subject_name")
    val subjectName: String? = null,

    @field:SerializedName("id_subject")
    val idSubject: Int? = null
) : Parcelable