package com.example.phoneverification.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.phoneverification.R
import com.example.phoneverification.data.NavigationListener
import com.example.phoneverification.data.PhoneAuthViewModel
import com.example.phoneverification.databinding.FragmentPhoneNumberBinding
import com.example.phoneverification.ui.base.BaseFragment


class PhoneNumberFragment : BaseFragment<FragmentPhoneNumberBinding, PhoneAuthViewModel>(),NavigationListener {

    override fun getFragmentView(): Int = R.layout.fragment_phone_number

    override fun getViewModel(): Class<PhoneAuthViewModel> = PhoneAuthViewModel::class.java

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.setNavigationListener(this)

        viewModel.progressBarVisible_PHONE.observe(viewLifecycleOwner, Observer {
            binding.progressBarNumberVerification.visibility = it
        })

        binding.verify.setOnClickListener(View.OnClickListener {
            viewModel.verify()
        })
    }

    override fun navigateToOTPFragment() {
        val action =
            PhoneNumberFragmentDirections.actionPhoneNumberFragmentToOTPFragment()
        findNavController()?.navigate(action)
    }
}