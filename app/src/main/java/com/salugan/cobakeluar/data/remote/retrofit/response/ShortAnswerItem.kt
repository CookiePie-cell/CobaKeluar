package com.salugan.cobakeluar.data.remote.retrofit.response

import com.google.gson.annotations.SerializedName

data class ShortAnswerItem(

    @field:SerializedName("second_range")
    val secondRange: String? = null,

    @field:SerializedName("short_answer_text")
    val shortAnswerText: String? = null,

    @field:SerializedName("first_range")
    val firstRange: String? = null,

    @field:SerializedName("question_id")
    val questionId: Int? = null,

    @field:SerializedName("id_short_answer")
    val idShortAnswer: Int? = null
)