package com.example.phoneverification.data

import android.content.Context

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class PhoneAuthViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhoneAuthViewModel::class.java)){
            return PhoneAuthViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}