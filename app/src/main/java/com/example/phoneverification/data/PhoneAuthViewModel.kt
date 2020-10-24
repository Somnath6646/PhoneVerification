package com.example.phoneverification.data

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
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
import java.util.concurrent.TimeUnit

class PhoneAuthViewModel(private val context: Context) : ViewModel(), Observable{

    private lateinit var verificationID: String
    private lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var credential: PhoneAuthCredential
    private var verificationFailed:Boolean = false

    @Bindable
    val phoneNumber = MutableLiveData<String>()

    @Bindable
    val otp = MutableLiveData<String>()

    private val fireBaseAuth by lazy{
        FirebaseAuth.getInstance()
    }


    private val validationMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
    get() = validationMessage


    fun goToPhoneNumberFragment(view: View){
        val action = IntroFragmentDirections.actionIntroFragmentToPhoneNumberFragment()
        view.findNavController().navigate(action)
    }

    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            Log.i("NUM_VERIFICATION_STATE", "PhoneNumberVerificationState.SUCESS")
            validationMessage.value = Event("OTP Verification done")
        }

        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {

                validationMessage.value = Event("Invalid phone number.")
            } else if (e is FirebaseTooManyRequestsException) {

                validationMessage.value = Event("Quota exceeded.")
            }
            verificationFailed = true

            validationMessage.value = Event("Verification failed")
            Log.i("error", e.toString())
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            verificationID = p0
            resendingToken = p1
            validationMessage.value = Event("Code Sent")
            Log.i("NUM_VERIFICATION_STATE", "PhoneNumberVerificationState.CODE_SENT")
        }

    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            context as Activity, // Activity (for callback binding)
            callbacks, // OnVerificationStateChangedCallbacks
            token) // ForceResendingToken from callbacks
    }



   fun verify(view: View){
       if(phoneNumber.value == null){
           validationMessage.value= Event("Please enter a phone number")
       }
       else if( !Patterns.PHONE.matcher(phoneNumber.value).matches()){
           validationMessage.value= Event("Please enter a proper phone number")
       }else {
           PhoneAuthProvider.getInstance().verifyPhoneNumber(
               phoneNumber.value!!,
               60,
               TimeUnit.SECONDS,
               context as Activity,
               callbacks
           )

           //navigate to otp fragment
           if(!verificationFailed) {
               val action = PhoneNumberFragmentDirections.actionPhoneNumberFragmentToOTPFragment()
               view.findNavController().navigate(action)
               verificationFailed = false
           }
       }

   }





    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        fireBaseAuth.signInWithCredential(credential).addOnCompleteListener(){task ->
            if (task.isSuccessful){
                validationMessage.value = Event("OTP verification Done")
            }
            else{
                Log.i("Error", task.exception.toString())
                validationMessage.value = Event("OTP verification failed")
            }
        }
    }


    fun verifyPhoneNumberWithCode(view: View) {
        val credential = PhoneAuthProvider.getCredential(verificationID, otp.value!!)
        signInWithPhoneAuthCredential(credential)
    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}