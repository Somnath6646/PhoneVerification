package com.example.phoneverification.ui.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.phoneverification.R
import com.example.phoneverification.data.PhoneAuthViewModel
import com.example.phoneverification.databinding.FragmentPhoneNumberBinding
import com.example.phoneverification.ui.base.BaseFragment


class PhoneNumberFragment : BaseFragment<FragmentPhoneNumberBinding, PhoneAuthViewModel>() {

    override fun getFragmentView(): Int = R.layout.fragment_phone_number

    override fun getViewModel(): Class<PhoneAuthViewModel> = PhoneAuthViewModel::class.java

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled().let {
                if (it != null)
                    Toast.makeText(context, it , Toast.LENGTH_SHORT).show()

            }

        })
    }
}