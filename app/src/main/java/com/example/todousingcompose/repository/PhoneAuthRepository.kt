package com.example.todousingcompose.repository

import android.app.Activity
import com.example.todousingcompose.util.Resource
import kotlinx.coroutines.flow.Flow

interface PhoneAuthRepository {

    fun createUserWithPhone(
        phone:String,
        activity: Activity
    ) : Flow<Resource<String>>

    fun signWithCredential(
        otp:String
    ): Flow<Resource<String>>

}