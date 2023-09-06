package com.salugan.cobakeluar.ui.activity.soal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.salugan.cobakeluar.data.TryoutRepository
import com.salugan.cobakeluar.model.QuestionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.salugan.cobakeluar.data.Result

@HiltViewModel
class SoalViewModel @Inject constructor(private val tryoutRepository: TryoutRepository) : ViewModel(){

    fun getDataDanKetidakPastianQuestion(): LiveData<Result<List<QuestionModel>>> {
        return tryoutRepository.getDataDanKetidakPastianQuestions()
    }
}