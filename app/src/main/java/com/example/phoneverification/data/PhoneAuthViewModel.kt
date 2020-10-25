package com.example.phoneverification.data

import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.phoneverification.ui.fragments.IntroFragmentDirections
import com.example.phoneverification.ui.fragments.PhoneNumberFragmentDirections
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class PhoneAuthViewModel(private val firebaseAuthSource: FirebaseAuthSource) : ViewModel(), Observable, AuthCallBackListener{

    private lateinit var navigateListener: NavigationListener

    fun setNavigationListener(navlistener: NavigationListener) {
        this.navigateListener = navlistener
    }

    var progressBarVisible_OTP: MutableLiveData<Int> = MutableLiveData<Int>(View.GONE)
    var progressBarVisible_PHONE: MutableLiveData<Int> = MutableLiveData<Int>(View.GONE)
    @Bindable
    val phoneNumber = MutableLiveData<String>()

    @Bindable
    val otp = MutableLiveData<String>()

    private val fireBaseAuth by lazy{
        FirebaseAuth.getInstance()
    }

    init {
        firebaseAuthSource.setAuthCallBackListener(this)
    }


    private val validationMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
    get() = validationMessage



    private fun resendVerificationCode(
        phoneNumber: String
    ) {

    }




   fun verify(){


       if(phoneNumber.value == null){
           validationMessage.value= Event("Please enter a phone number")

       }
       else if( !Patterns.PHONE.matcher(phoneNumber.value).matches()){
           validationMessage.value= Event("Please enter a proper phone number")
         }

       else {
           progressBarVisible_PHONE.value = View.VISIBLE
           firebaseAuthSource.sendVerificationCode(phoneNumber.value!!)
       }


   }


    fun verifyOTP(){
        progressBarVisible_OTP.value = View.VISIBLE
        firebaseAuthSource.verifyVerificationCode(otp.value!!)
    }


    override fun onVerificationCompleted(credential: PhoneAuthCredential) {

        validationMessage.value = Event("OTP Verification done automatically")

        progressBarVisible_OTP .value = View.VISIBLE
        progressBarVisible_PHONE.value = View.GONE
    }

    override fun onVerificationFailed(e: FirebaseException) {

        if (e is FirebaseAuthInvalidCredentialsException) {
            validationMessage.value = Event("Invalid request")
        } else if (e is FirebaseTooManyRequestsException) {
            validationMessage.value = Event("SMS quota exceeded")
        } else{
            validationMessage.value = Event("Phone number verification failed")
        }

    }

    override fun onCodeSent(
        verificationId: String,
        resendingToken: PhoneAuthProvider.ForceResendingToken
    ) {
        progressBarVisible_PHONE.value = View.GONE
        validationMessage.value = Event("Code Sent")
        navigateListener.navigateToOTPFragment()

    }

    override fun signInSucess() {

        validationMessage.value = Event("Sign In sucessfull")
        progressBarVisible_OTP.value = View.GONE
    }

    override fun signInFailed(e: Exception) {

        validationMessage.value = Event("Sign In Failed")
        progressBarVisible_OTP.value = View.GONE
    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }



}

interface NavigationListener{
    fun navigateToOTPFragment()
}