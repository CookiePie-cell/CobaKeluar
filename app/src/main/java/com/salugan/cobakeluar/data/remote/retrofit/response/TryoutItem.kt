package com.salugan.cobakeluar.data.remote.retrofit.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TryoutItem(

    @field:SerializedName("subject_id")
    val subjectId: Int? = null,

    @field:SerializedName("category_name")
    val categoryName: String? = null,

    @field:SerializedName("category_id")
    val categoryId: Int? = null,

    @field:SerializedName("question")
    val question: List<QuestionItem>,

    @field:SerializedName("subject_name")
    val subjectName: String? = null,

    @field:SerializedName("is_fav")
    val isFav: Boolean? = null,

    @field:SerializedName("id")
    val id: Int? = null
) : Parcelable