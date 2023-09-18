package com.salugan.cobakeluar.ui.activity.soal

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.salugan.cobakeluar.data.TryoutRepository
import com.salugan.cobakeluar.model.QuestionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.salugan.cobakeluar.data.Result
import com.salugan.cobakeluar.data.local.TryoutManager
import kotlinx.coroutines.launch

@HiltViewModel
class SoalViewModel @Inject constructor(
    private val tryoutRepository: TryoutRepository,
    private val tryoutManager: TryoutManager
) : ViewModel(){

    private var timer: CountDownTimer? = null

    private val initialTime = MutableLiveData<Long>()
    private val currentTime = MutableLiveData<Long>()

    val currentTimeString = currentTime.map { time ->
        DateUtils.formatElapsedTime(time / 1000)
    }

    private val _eventCountDownFinish = MutableLiveData<Boolean>()
    val eventCountDownFinish: LiveData<Boolean> = _eventCountDownFinish

    /**
     * this method is to set the initial time of the tryout timer.
     * @param minuteFocus variable to indicate how many minutes the user want to set as the initial time.
     * if [fromResetPassword] 30, the initial time will be set to 30 minutes.
     * @author Julio Nicholas.
     * @since 11 September 2023.
     * */
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


    /**
     * this method is to set the total time of the timer.
     * @param minutes variable to indicate how many minutes the user want to set as the total time.
     * if [fromResetPassword] 30, the total time will be set to 30 minutes.
     * @author Julio Nicholas.
     * @since 11 September 2023.
     * */
    fun setTotalExamTime(minutes: Long) {
        totalExamTimeMillis.value = minutes * 60 * 1000
    }

    /**
     * this method is to calculate how long the timer have been running.
     * @author Julio Nicholas.
     * @since 11 September 2023.
     * */
    fun calculateCompletionTime(): Long {
        // Check if it's the last item
        val remainingTimeMillis = currentTime.value ?: 0
        val totalExamTimeMillis = totalExamTimeMillis.value ?: 0
        return totalExamTimeMillis - remainingTimeMillis
    }

    /**
     * this method is to start the timer countdown.
     * @author Julio Nicholas.
     * @since 11 September 2023.
     * */
    fun startTimer() {
        timer?.start()
    }

    /**
     * this method is to stop the timer countdown.
     * @author Julio Nicholas.
     * @since 11 September 2023.
     * */
    fun stopTimer() {
        timer?.cancel()
    }

    /**
     * this method is to reset the timer countdown.
     * @author Julio Nicholas.
     * @since 11 September 2023.
     * */
    fun resetTimer() {
        timer?.cancel()
        _eventCountDownFinish.value = true
    }

    /**
     * this method to get the "Data dan Ketidakpastian" data from repository as a livedata.
     * @author Julio Nicholas.
     * @since 5 September 2023.
     * */
    fun getDataDanKetidakPastianQuestion(): LiveData<Result<List<QuestionModel>>> {
        return tryoutRepository.getDataDanKetidakPastianQuestions()
    }

    /**
     * this method to get the "Geometri dan Pengukuran" data from repository as a livedata.
     * @author Julio Nicholas.
     * @since 5 September 2023.
     * */
    fun getGeometriDanPengukuranQuestion(): LiveData<Result<List<QuestionModel>>> {
        return tryoutRepository.getGeometriDanPengukuranQuestion()
    }

    fun getTryoutStatus(): LiveData<Boolean> = tryoutManager.tryoutFinished.asLiveData()

    fun setTryoutStatus(finished: Boolean) {
        viewModelScope.launch {
            tryoutManager.setTryoutFinished(finished)
        }
    }

    /**
     * this method will be called when this ViewModel is no longer used and will be destroyed.
     * @author Julio Nicholas.
     * @since 5 September 2023.
     * */
    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}

