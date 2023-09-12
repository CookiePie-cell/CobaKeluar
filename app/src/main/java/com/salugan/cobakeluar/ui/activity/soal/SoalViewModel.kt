package com.salugan.cobakeluar.ui.activity.soal

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.salugan.cobakeluar.data.TryoutRepository
import com.salugan.cobakeluar.model.QuestionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.salugan.cobakeluar.data.Result

@HiltViewModel
class SoalViewModel @Inject constructor(private val tryoutRepository: TryoutRepository) : ViewModel(){

    private var timer: CountDownTimer? = null

    private val initialTime = MutableLiveData<Long>()
    private val currentTime = MutableLiveData<Long>()

    val currentTimeString = currentTime.map { time ->
        DateUtils.formatElapsedTime(time / 1000)
    }

    private val _eventCountDownFinish = MutableLiveData<Boolean>()
    val eventCountDownFinish: LiveData<Boolean> = _eventCountDownFinish

    fun setInitialTime(minuteFocus: Long) {
        val initialTimeMillis = minuteFocus * 60 * 1000
        initialTime.value = initialTimeMillis
        currentTime.value = initialTimeMillis

        timer = object : CountDownTimer(initialTimeMillis, 1000) {
            override fun onTick(p0: Long) {
                currentTime.value = p0
            }

            override fun onFinish() {
                resetTimer()
            }
        }
    }

    private val totalExamTimeMillis = MutableLiveData<Long>()

    fun setTotalExamTime(minutes: Long) {
        totalExamTimeMillis.value = minutes * 60 * 1000
    }

    fun calculateCompletionTime(): Long {
        // Check if it's the last item
        val remainingTimeMillis = currentTime.value ?: 0
        val totalExamTimeMillis = totalExamTimeMillis.value ?: 0
        return totalExamTimeMillis - remainingTimeMillis
    }

    fun startTimer() {
        timer?.start()
    }

    fun resetTimer() {
        timer?.cancel()
        _eventCountDownFinish.value = true
    }

    fun getDataDanKetidakPastianQuestion(): LiveData<Result<List<QuestionModel>>> {
        return tryoutRepository.getDataDanKetidakPastianQuestions()
    }

    fun getGeometriDanPengukuranQuestion(): LiveData<Result<List<QuestionModel>>> {
        return tryoutRepository.getGeometriDanPengukuranQuestion()
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}