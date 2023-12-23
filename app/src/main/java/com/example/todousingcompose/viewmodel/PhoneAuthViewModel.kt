package com.example.todousingcompose.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.todousingcompose.repository.PhoneAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    private val repository: PhoneAuthRepository
): ViewModel() {

    fun createUserWithPhone(
        mobile:String,
        activity: Activity
    ) = repository.createUserWithPhone(mobile,activity)

    fun signInWithCredential(
        code:String
    ) = repository.signWithCredential(code)

}