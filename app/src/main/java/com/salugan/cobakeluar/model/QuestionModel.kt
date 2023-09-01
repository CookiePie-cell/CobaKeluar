package com.salugan.cobakeluar.model

import com.salugan.cobakeluar.data.remote.retrofit.response.DiscussionItem
import com.salugan.cobakeluar.data.remote.retrofit.response.SelectionAnswerItem
import com.salugan.cobakeluar.data.remote.retrofit.response.ShortAnswerItem

data class QuestionModel(
    val id: Int?,
    val tryoutId: Int?,
    val questionId: Int?,
    val qtId: Int?,
    val essayAnswer: Any?,
    val selections: List<SelectionModel>?,
    val selectionAnswer: List<SelectionAnswerItem?>?,
    val keyword: List<Any?>?,
    val shortAnswer: List<ShortAnswerItem?>?,
    val discussion: List<DiscussionItem?>?,
    val statementQuestion: List<Any?>?,
    val isEssay: Boolean,
    val isMultipleChoice: Boolean,
    val isMultipleCorrectChoice: Boolean
)