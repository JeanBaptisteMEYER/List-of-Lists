package com.jbm.mistplaychallenge.ui.bonus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BonusViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Bonus View"
    }
    val text: LiveData<String> = _text
}