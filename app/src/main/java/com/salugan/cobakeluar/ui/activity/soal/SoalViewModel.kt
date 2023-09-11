package com.salugan.cobakeluar.ui.activity.soal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.salugan.cobakeluar.data.TryoutRepository
import com.salugan.cobakeluar.model.QuestionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.salugan.cobakeluar.data.Result
import com.salugan.cobakeluar.model.SelectionModel

@HiltViewModel
class SoalViewModel @Inject constructor(private val tryoutRepository: TryoutRepository) : ViewModel(){

    val soalState: MutableMap<Int, SelectionModel> = mutableMapOf()

    fun getDataDanKetidakPastianQuestion(): LiveData<Result<List<QuestionModel>>> {
        return tryoutRepository.getDataDanKetidakPastianQuestions()
    }

    fun getGeometriDanPengukuranQuestion(): LiveData<Result<List<QuestionModel>>> {
        return tryoutRepository.getGeometriDanPengukuranQuestion()
    }
}