package com.ykstar.bangkit.taniland.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ykstar.bangkit.taniland.models.BibitResponse
import com.ykstar.bangkit.taniland.repositories.BibitRepository
import com.ykstar.bangkit.taniland.utils.Resource
import kotlinx.coroutines.launch

class BibitViewModel : ViewModel() {
    private val bibitRepository = BibitRepository()

    private val _bibitResponseState = MutableLiveData<Resource<BibitResponse?>>()
    val bibitResponse: LiveData<Resource<BibitResponse?>>
        get() = _bibitResponseState

    fun getAllBibit(token: String) {
        viewModelScope.launch {
            val resource = bibitRepository.getBibit(token)
            _bibitResponseState.value = resource
        }
    }
}