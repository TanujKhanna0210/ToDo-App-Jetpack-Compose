package com.example.todousingcompose.util

sealed class Resource<out T> {

    data class Success<out R>(val data: R) : Resource<R>()
    data class Failure(val message: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()


}
