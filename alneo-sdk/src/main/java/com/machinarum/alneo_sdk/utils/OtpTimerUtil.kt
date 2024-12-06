package com.machinarum.alneo_sdk.utils

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class OtpTimerUtil {
    private var expireDuration = 60000
    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String> get() = _formattedTime

    private val _timeFinished = MutableLiveData<Boolean>()
    val timeFinished: LiveData<Boolean> get() = _timeFinished

    private val _countDown = MutableLiveData<Int>()
    val countDown: LiveData<Int> get() = _countDown

    private lateinit var timer: CountDownTimer

    fun start(time: Int = 60000) {
        stop()
        expireDuration = time

        timer = object : CountDownTimer(time + 0L, 1000) {
            override fun onTick(l: Long) {
                calculate(l)
            }

            override fun onFinish() {
                _timeFinished.value = true
            }
        }

        timer.start()
    }

    private fun stop() {
        if (this::timer.isInitialized)
            timer.cancel()
        _timeFinished.value = false
    }


    private fun calculate(timer: Long) {
        val totalSecond = timer / 1000

        val second = totalSecond % 60
        val minute = totalSecond / 60

        _countDown.value = expireDuration + 1000 - timer.toInt()
        _formattedTime.value = "%02d:%02d".format(minute, second)
    }
}