package com.ykstar.bangkit.taniland.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ykstar.bangkit.taniland.models.LahanResponse
import com.ykstar.bangkit.taniland.repositories.LahanRepository
import com.ykstar.bangkit.taniland.utils.Resource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ykstar.bangkit.taniland.models.DetailLahanResponse
import com.ykstar.bangkit.taniland.models.LahanRequest
import kotlinx.coroutines.launch

class LahanViewModel : ViewModel() {
    private val lahanRepository = LahanRepository()

    private val _lahanResponseState = MutableLiveData<Resource<LahanResponse?>>()
    val lahanResponse: LiveData<Resource<LahanResponse?>>
        get() = _lahanResponseState

    private val _lahanTambah = MutableLiveData<Resource<LahanResponse?>>()
    val lahanTambah: LiveData<Resource<LahanResponse?>> get() = _lahanTambah

    private val _lahanDetail = MutableLiveData<Resource<DetailLahanResponse?>>()
    val lahanDetail: LiveData<Resource<DetailLahanResponse?>>
        get() = _lahanDetail

    fun tambahLahan(token: String?, lahan: LahanRequest) = viewModelScope.launch {
        _lahanTambah.value = lahanRepository.tambahLahan(token, lahan)
    }

    fun getLahanData(token: String) {
        viewModelScope.launch {
            val resource = lahanRepository.getLahan(token)
            _lahanResponseState.value = resource
        }
    }

    fun getLahanDetail(id: String, token: String) = viewModelScope.launch {
        viewModelScope.launch {
            val resource = lahanRepository.getLahanDetail(id, token)

            if (resource is Resource.Success) {
                _lahanDetail.value = resource
            }
        }

    }

}
