package com.salugan.cobakeluar.data.remote.retrofit.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize

data class DiscussionItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("discussion_text")
    val discussionText: String? = null,

    @field:SerializedName("question_id")
    val questionId: Int? = null,

    @field:SerializedName("id_discussion")
    val idDiscussion: Int? = null
) : Parcelable
