package com.ykstar.bangkit.taniland.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ykstar.bangkit.taniland.models.HasilIoTResponse
import com.ykstar.bangkit.taniland.models.WithoutDataResponseModel
import com.ykstar.bangkit.taniland.repositories.IoTRepository
import com.ykstar.bangkit.taniland.utils.Resource
import kotlinx.coroutines.launch

class IoTViewModel : ViewModel() {
    private val iotRepository = IoTRepository()

    private val _iotRegResResponseState = MutableLiveData<Resource<WithoutDataResponseModel?>>()
    val iotRegResResponse: LiveData<Resource<WithoutDataResponseModel?>>
        get() = _iotRegResResponseState

    private val _hasilIoTResponseState = MutableLiveData<Resource<HasilIoTResponse?>>()
    val hasilIoTResponse: LiveData<Resource<HasilIoTResponse?>>
        get() = _hasilIoTResponseState

    fun registIoT(
        token: String?,
        iotId: String,
        lahanId: String
    ) {
        viewModelScope.launch {
            _iotRegResResponseState.value =
                iotRepository.regitIoT(token, iotId, lahanId)
        }
    }

    fun getHasilIoT(
        iotId: String,
        token: String?
    ) {
        viewModelScope.launch {
            _hasilIoTResponseState.value =
                iotRepository.getHasilIoT(iotId, token)
        }
    }

    fun resetIoT(
        token: String?,
        iotId: String,
    ) {
        viewModelScope.launch {
            _iotRegResResponseState.value =
                iotRepository.resetIoT(token, iotId)
        }
    }

}