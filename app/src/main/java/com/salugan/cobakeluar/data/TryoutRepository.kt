package com.salugan.cobakeluar.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.salugan.cobakeluar.data.remote.retrofit.ApiService
import com.salugan.cobakeluar.model.QuestionModel
import com.salugan.cobakeluar.utils.DataMapper

class TryoutRepository(private val apiService: ApiService) {

    /**
     * this method to get "Data dan Ketidakpastian" tryout data from API, filter it,and map it to custom model
     * @author Julio Nicholas
     * @since 1 September 2023.
     * Updated 14 September 2023 by Julio Nicholas
     * */
    fun getDataDanKetidakPastianQuestions(): LiveData<Result<List<QuestionModel>>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.getTryOut()

            if (response.isSuccessful) {
                val data = response.body()?.data
                if (data != null) {
                    val question = data
                        .filter { it.idSubject == 6 }[0]
                        .tryout
                        .filter { it.id == 30 }[0]
                        .question
                    val mappedQuestion = DataMapper.mapQuestionItemToModel(question)
                    emit(Result.Success(mappedQuestion))
                }
            } else {
                val errorBody = response.errorBody()
                val errorMessage = errorBody?.string() ?: "Unknown error"
                val errorCode = response.code()
                emit(Result.Error(Error(errorCode, errorMessage)))
            }
        } catch (e: Exception) {
            val errorMessage = "Check your internet connection"
            emit(Result.Error(Error(-1, errorMessage)))
        }
    }


    /**
     * this method to get "Geometri dan Pengukuran" tryout data from API, filter it,and map it to custom model
     * @author Julio Nicholas
     * @since 1 September 2023.
     * Updated 14 September 2023 by Julio Nicholas
     * */
    fun getGeometriDanPengukuranQuestion(): LiveData<Result<List<QuestionModel>>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.getTryOut()
            if (response.isSuccessful) {
                val data = response.body()?.data
                if (data != null) {
                    val question = data
                        .filter { it.idSubject == 6 }[0]
                        .tryout
                        .filter { it.id == 31 }[0]
                        .question

                    val mappedQuestion = DataMapper.mapQuestionItemToModel(question)
                    emit(Result.Success(mappedQuestion))
                }
            } else {
                val errorBody = response.errorBody()
                val errorMessage = errorBody?.string() ?: "Unknown error"
                val errorCode = response.code()
                emit(Result.Error(Error(errorCode, errorMessage)))
            }
        } catch(exception: Exception) {
            val errorMessage = "Check your internet connection"
            emit(Result.Error(Error(-1, errorMessage)))
        }
    }
}