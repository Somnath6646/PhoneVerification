package com.example.phoneverification.data

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.lang.Exception

interface AuthCallBackListener {

    fun onVerificationCompleted(credential: PhoneAuthCredential)
    fun onVerificationFailed(e: FirebaseException)
    fun onCodeSent(verificationId: String, resendingToken: PhoneAuthProvider.ForceResendingToken)
    fun signInSucess()
    fun signInFailed(e: Exception)
}