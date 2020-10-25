package com.example.phoneverification.ui

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.phoneverification.R
import com.example.phoneverification.data.FirebaseAuthSource
import com.example.phoneverification.data.PhoneAuthViewModel
import com.example.phoneverification.data.PhoneAuthViewModelFactory
import kotlinx.android.synthetic.main.fragment_intro.*

class PhoneAuthActivity : AppCompatActivity(){



    private lateinit var viewModel: PhoneAuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val firebaseAuthSource = FirebaseAuthSource()
        val factory = PhoneAuthViewModelFactory(firebaseAuthSource)
        viewModel = ViewModelProvider(this,factory).get(PhoneAuthViewModel::class.java)
        viewModel.message.observe(this, Observer {
            it.getContentIfNotHandled().let {
                if (it != null)
                    Toast.makeText(this, it , Toast.LENGTH_SHORT).show()

            }


        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.phoneNumber.value=""
        viewModel.otp.value=""
    }
}