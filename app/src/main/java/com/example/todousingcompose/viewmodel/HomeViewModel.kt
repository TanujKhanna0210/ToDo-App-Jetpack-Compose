package com.example.todousingcompose.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todousingcompose.model.FirestoreResponse
import com.example.todousingcompose.repository.HomeRepository
import com.example.todousingcompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
): ViewModel(){

    private val _res: MutableState<FirestoreState> = mutableStateOf(FirestoreState())
    val res: State<FirestoreState> = _res

    fun insert(item:FirestoreResponse) = repository.insert(item)

    private val _updateData: MutableState<FirestoreResponse> = mutableStateOf(
        FirestoreResponse()
    )
    val updateData: State<FirestoreResponse> = _updateData

    fun setData(data:FirestoreResponse){
        _updateData.value = data
    }

    init {
        getItems()
    }

    fun getItems() = viewModelScope.launch {
        repository.getItems().collect{
            when(it){
                is Resource.Success->{
                    _res.value = FirestoreState(
                        data = it.data
                    )
                }
                is Resource.Failure->{
                    _res.value = FirestoreState(
                        error = it.message.toString()
                    )
                }
                is Resource.Loading->{
                    _res.value = FirestoreState(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun delete(key:String) = repository.delete(key)
    fun update(item:FirestoreResponse) = repository.update(item)

}

data class FirestoreState(
    val data:List<FirestoreResponse> = emptyList(),
    val error:String = "",
    val isLoading:Boolean = false
)