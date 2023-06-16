package com.ykstar.bangkit.taniland.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ykstar.bangkit.taniland.models.RiwayatTanamResponse
import com.ykstar.bangkit.taniland.models.WithoutDataResponseModel
import com.ykstar.bangkit.taniland.repositories.TanamRepository
import com.ykstar.bangkit.taniland.utils.Resource
import kotlinx.coroutines.launch

class TanamViewModel : ViewModel() {
    private val tanamRepository = TanamRepository()

    private val _statusTanamResponseState = MutableLiveData<Resource<WithoutDataResponseModel?>>()
    val statusTanamResponse: LiveData<Resource<WithoutDataResponseModel?>>
        get() = _statusTanamResponseState

    private val _deleteTanamResponseState = MutableLiveData<Resource<WithoutDataResponseModel?>>()
    val deleteTanamResponse: LiveData<Resource<WithoutDataResponseModel?>>
        get() = _statusTanamResponseState

    private val _riwayatTanamResponseState = MutableLiveData<Resource<RiwayatTanamResponse?>>()
    val riwayatTanamResponse: LiveData<Resource<RiwayatTanamResponse?>>
        get() = _riwayatTanamResponseState

    fun statusTanamPlan(
        token: String?,
        bibitId: String,
        lahanId: String
    ) {
        viewModelScope.launch {
            _statusTanamResponseState.value =
                tanamRepository.statusTanamPlan(token, bibitId, lahanId)
        }
    }

    fun statusTanamExec(
        token: String?, id: String?,
        jarak: Int?,
        tanggal_tanam: String?
    ) {
        viewModelScope.launch {
            _statusTanamResponseState.value =
                tanamRepository.statusTanamExec(token, id, jarak, tanggal_tanam)
        }
    }

    fun statusTanamClose(
        token: String?, id: String?,
        tanggalPanen: String?,
        jumlahPanen: Int?,
        hargaPanen: Int?,
    ) {
        viewModelScope.launch {
            _statusTanamResponseState.value =
                tanamRepository.statusTanamClose(token, id, tanggalPanen, jumlahPanen, hargaPanen)
        }
    }

    fun deleteTanam(token: String?, tanamId: String) {
        viewModelScope.launch {
            _deleteTanamResponseState.value = tanamRepository.deleteTanam(token, tanamId)
        }
    }

    fun getRiwayatTanam(token: String, lahanId: String) {
        viewModelScope.launch {
            val resource = tanamRepository.getRiwayatTanam(token, lahanId)
            if (resource is Resource.Success) {
                _riwayatTanamResponseState.value = resource
            }
        }
    }
}