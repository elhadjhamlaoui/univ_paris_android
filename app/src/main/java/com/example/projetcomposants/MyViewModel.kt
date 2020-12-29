package com.example.projetcomposants

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {


    val lvLink : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val lvPopupWindowIsDisplayed : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
}