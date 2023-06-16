package com.ykstar.bangkit.taniland.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ykstar.bangkit.taniland.models.LahanRequest
import com.ykstar.bangkit.taniland.repositories.LahanRepository
import com.ykstar.bangkit.taniland.utils.Resource
import kotlinx.coroutines.launch

class LahanViewModel : ViewModel() {
    private val lahanRepository = LahanRepository()

    sealed class LahanState {
        object Loading : LahanState()
        data class Success<T>(val data: T) : LahanState()
        data class Error(val message: String) : LahanState()
    }

    private val _lahanState = MutableLiveData<LahanState>()
    val lahanState: LiveData<LahanState> get() = _lahanState

    fun tambahLahan(token: String?, lahan: LahanRequest) = viewModelScope.launch {
        _lahanState.value = LahanState.Loading
        val resource = lahanRepository.tambahLahan(token, lahan)
        if (resource is Resource.Success) {
            _lahanState.value = LahanState.Success(resource)
        } else if (resource is Resource.Error) {
            _lahanState.value = LahanState.Error(resource.exception?.message.toString())
        }
    }

    fun getLahanData(token: String) {
        viewModelScope.launch {
            _lahanState.value = LahanState.Loading
            val resource = lahanRepository.getLahan(token)
            if (resource is Resource.Success) {
                _lahanState.value = LahanState.Success(resource)
            } else if (resource is Resource.Error) {
                _lahanState.value = LahanState.Error(resource.exception?.message.toString())
            }
        }
    }

    fun getLahanDetail(id: String, token: String) = viewModelScope.launch {
        viewModelScope.launch {
            _lahanState.value = LahanState.Loading
            val resource = lahanRepository.getLahanDetail(id, token)
            if (resource is Resource.Success) {
                _lahanState.value = LahanState.Success(resource)
            } else if (resource is Resource.Error) {
                _lahanState.value = LahanState.Error(resource.exception?.message.toString())
            }
        }
    }

    fun deleteLahan(token: String?, lahan_id: String) = viewModelScope.launch {
        _lahanState.value = LahanState.Loading
        val resource = lahanRepository.deleteLahan(token, lahan_id)
        if (resource is Resource.Success) {
            _lahanState.value = LahanState.Success(resource)
        } else if (resource is Resource.Error) {
            _lahanState.value = LahanState.Error(resource.exception?.message.toString())
        }
    }

}
