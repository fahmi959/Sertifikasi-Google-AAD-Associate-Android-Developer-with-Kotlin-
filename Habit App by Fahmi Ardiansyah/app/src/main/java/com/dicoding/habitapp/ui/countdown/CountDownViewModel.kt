package com.dicoding.habitapp.ui.countdown


import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class CountDownViewModel : ViewModel() {

    private var timer: CountDownTimer? = null

    private val initialTime = MutableLiveData<Long>()
    private val currentTime = MutableLiveData<Long>()

    // The String version of the current time (hh:mm:ss)
    val currentTimeString = currentTime.map { time ->
        DateUtils.formatElapsedTime(time / 1000)
    }


    // Event which triggers the end of count down
    private val _eventCountDownFinish = MutableLiveData<Boolean>()
    val eventCountDownFinish: LiveData<Boolean> = _eventCountDownFinish

    fun setInitialTime(minuteFocus: Long) {
        val initialTimeMillis = minuteFocus * 60 * 1000
        initialTime.value = initialTimeMillis
        currentTime.value = initialTimeMillis

        timer = object : CountDownTimer(initialTimeMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                currentTime.value = millisUntilFinished
            }

            override fun onFinish() {
                resetTimer()
                _eventCountDownFinish.value = true
            }
        }
    }

    fun startTimer() {
        timer?.start()
    }

    fun resetTimer() {
        currentTime.value = initialTime.value
    }

    fun nextTimer() {
        val currentMillis = currentTime.value ?: return

        timer = object : CountDownTimer(currentMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                currentTime.value = millisUntilFinished
            }

            override fun onFinish() {
                resetTimer()
                _eventCountDownFinish.value = true
            }
        }

        startTimer()
    }


    fun cancelTimer() {
        timer?.cancel()
    }


    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}