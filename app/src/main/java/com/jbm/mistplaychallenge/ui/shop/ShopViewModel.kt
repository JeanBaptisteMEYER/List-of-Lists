package com.jbm.mistplaychallenge.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShopViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Shop View"
    }
    val text: LiveData<String> = _text
}