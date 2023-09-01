package com.salugan.cobakeluar.data.remote.retrofit.response

import com.google.gson.annotations.SerializedName

data class QuestionItem(

    @field:SerializedName("statement_question")
    val statementQuestion: List<Any?>? = null,

    @field:SerializedName("selection")
    val selection: List<SelectionItem?>? = null,

    @field:SerializedName("selection_answer")
    val selectionAnswer: List<SelectionAnswerItem?>? = null,

    @field:SerializedName("essay_answer")
    val essayAnswer: Any? = null,

    @field:SerializedName("short_answer")
    val shortAnswer: List<ShortAnswerItem?>? = null,

    @field:SerializedName("qt_id")
    val qtId: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("discussion")
    val discussion: List<DiscussionItem?>? = null,

    @field:SerializedName("tryout_id")
    val tryoutId: Int? = null,

    @field:SerializedName("keyword")
    val keyword: List<Any?>? = null,

    @field:SerializedName("question_id")
    val questionId: Int? = null
)