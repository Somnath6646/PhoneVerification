package com.example.phoneverification.data

import android.util.Log
import android.view.View
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class FirebaseAuthSource {
    private lateinit var storedVerificationId: String
    private lateinit var storedResendingToken: PhoneAuthProvider.ForceResendingToken

    private lateinit var callBackListener: AuthCallBackListener

    fun setAuthCallBackListener(listener: AuthCallBackListener) {
        this.callBackListener = listener
    }

    private val mAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("TAG", "onVerificationCompleted:$credential")
            callBackListener.onVerificationCompleted(credential)
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {

            Log.w("TAG", "onVerificationFailed", e)
            callBackListener.onVerificationFailed(e)
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            callBackListener.onCodeSent(verificationId, token)
            storedVerificationId = verificationId
            storedResendingToken = token

        }
    }

    fun sendVerificationCode(phone: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phone.trim(),
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            callbacks
        )
    }

    fun verifyVerificationCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(){task ->
            if (task.isSuccessful){
                callBackListener.signInSucess()
            }
            else{
                Log.i("Error", task.exception.toString())
                callBackListener.signInFailed(task.exception!!)
            }
        }
    }

    fun resendCode(phone: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phone,
            30,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            callbacks,
            storedResendingToken
        )
    }
}