package com.ykstar.bangkit.taniland.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ykstar.bangkit.taniland.models.LahanResponse
import com.ykstar.bangkit.taniland.repositories.LahanRepository
import com.ykstar.bangkit.taniland.utils.Resource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ykstar.bangkit.taniland.models.LahanRequest
import kotlinx.coroutines.launch

class LahanViewModel : ViewModel() {
    private val lahanRepository = LahanRepository()

    private val _lahanResponseState = MutableLiveData<Resource<LahanResponse?>>()
    val lahanResponse: LiveData<Resource<LahanResponse?>>
        get() = _lahanResponseState

    private val _lahanTambah = MutableLiveData<Resource<LahanResponse?>>()
    val lahanTambah: LiveData<Resource<LahanResponse?>> get() = _lahanTambah

    fun tambahLahan(token: String?, lahan: LahanRequest) = viewModelScope.launch {
        _lahanTambah.value = lahanRepository.tambahLahan(token, lahan)
    }

    fun getLahanData(token: String) {
        viewModelScope.launch {
            val resource = lahanRepository.getLahan(token)
            _lahanResponseState.value = resource
        }
    }
}
