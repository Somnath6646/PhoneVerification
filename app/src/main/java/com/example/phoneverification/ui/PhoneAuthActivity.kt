package com.example.phoneverification.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.phoneverification.R
import com.example.phoneverification.data.PhoneAuthViewModel
import com.example.phoneverification.data.PhoneAuthViewModelFactory

class PhoneAuthActivity : AppCompatActivity(){



    private lateinit var viewModel: PhoneAuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val factory = PhoneAuthViewModelFactory(this)
        viewModel = ViewModelProvider(this,factory).get(PhoneAuthViewModel::class.java)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.phoneNumber.value=""
        viewModel.otp.value=""
    }
}