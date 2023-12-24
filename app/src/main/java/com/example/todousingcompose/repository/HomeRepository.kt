package com.example.todousingcompose.repository

import com.example.todousingcompose.model.FirestoreResponse
import com.example.todousingcompose.util.Resource
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun insert(
        item:FirestoreResponse
    ) : Flow<Resource<String>>

    fun getItems() : Flow<Resource<List<FirestoreResponse>>>

    fun delete(key:String) : Flow<Resource<String>>

    fun update(
        item:FirestoreResponse
    ) : Flow<Resource<String>>

}