package com.leeeyou123.samplelivedata.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RandomViewModel : ViewModel() {

    private val random: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    fun obtainRandom(): MutableLiveData<Int> {
        return random
    }

}