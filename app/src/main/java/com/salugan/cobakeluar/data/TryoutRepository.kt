package com.salugan.cobakeluar.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.salugan.cobakeluar.data.remote.retrofit.ApiService
import com.salugan.cobakeluar.model.QuestionModel
import com.salugan.cobakeluar.utils.DataMapper

class TryoutRepository(private val apiService: ApiService) {

    fun getDataDanKetidakPastianQuestions(): LiveData<Result<List<QuestionModel>>> = liveData {
        emit(Result.Loading)
        try {
            val data = apiService.getTryOut().data

            val question = data
                .filter { it.idSubject == 6 }[0]
                .tryout
                .filter { it.id == 30 }[0]
                .question

            val mappedQuestion = DataMapper.mapQuestionItemToModel(question)
            emit(Result.Success(mappedQuestion))
        } catch (e: Exception) {
            Log.e("ERRORRRRR", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getGeometriDanPengukuranQuestion(): LiveData<Result<List<QuestionModel>>> = liveData {
        emit(Result.Loading)
        try {
            val data = apiService.getTryOut().data

            val question = data
                .filter { it.idSubject == 6 }[0]
                .tryout
                .filter { it.id == 31 }[0]
                .question

            val mappedQuestion = DataMapper.mapQuestionItemToModel(question)
            emit(Result.Success(mappedQuestion))
        } catch (e: Exception) {
            Log.e("ERRORRRRR", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }
}