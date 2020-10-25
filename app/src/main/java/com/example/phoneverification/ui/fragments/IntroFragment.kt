package com.example.phoneverification.ui.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.phoneverification.R
import com.example.phoneverification.data.PhoneAuthViewModel
import com.example.phoneverification.databinding.FragmentIntroBinding
import com.example.phoneverification.databinding.FragmentPhoneNumberBinding
import com.example.phoneverification.ui.base.BaseFragment

class IntroFragment : BaseFragment<FragmentIntroBinding, PhoneAuthViewModel>() {

    override fun getFragmentView(): Int = R.layout.fragment_intro

    override fun getViewModel(): Class<PhoneAuthViewModel> = PhoneAuthViewModel::class.java

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        binding.proceed.setOnClickListener {
            val action = IntroFragmentDirections.actionIntroFragmentToPhoneNumberFragment()
            findNavController().navigate(action)
        }
    }
}