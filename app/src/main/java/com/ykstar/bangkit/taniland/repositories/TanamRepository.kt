package com.ykstar.bangkit.taniland.repositories

import com.ykstar.bangkit.taniland.models.RiwayatTanamResponse
import com.ykstar.bangkit.taniland.models.StatusTanamCloseRequest
import com.ykstar.bangkit.taniland.models.StatusTanamExecRequest
import com.ykstar.bangkit.taniland.models.StatusTanamPlanRequest
import com.ykstar.bangkit.taniland.models.WithoutDataResponseModel
import com.ykstar.bangkit.taniland.network.ApiConfig
import com.ykstar.bangkit.taniland.network.services.TanamApiService
import com.ykstar.bangkit.taniland.utils.Resource

class TanamRepository {
    private val apiService = ApiConfig.createService(TanamApiService::class.java)

    suspend fun statusTanamPlan(
        token: String?,
        bibitId: String,
        tanamId: String,
    ): Resource<WithoutDataResponseModel?> {
        return try {
            val requestBody = StatusTanamPlanRequest(bibitId, tanamId)
            val response = apiService.statusTanamPlan(BEARER + token, requestBody)
            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun statusTanamExec(
        token: String?,
        id: String?,
        jarak: Int?,
        tanggalTanam: String?,
    ): Resource<WithoutDataResponseModel?> {
        return try {
            val requestBody = StatusTanamExecRequest(id, jarak, tanggalTanam)
            val response = apiService.statusTanamExec(BEARER + token, requestBody)
            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun statusTanamClose(
        token: String?,
        id: String?,
        tanggalPanen: String?,
        jumlahPanen: Int?,
        hargaPanen: Int?,
    ): Resource<WithoutDataResponseModel?> {
        return try {
            val requestBody = StatusTanamCloseRequest(id, tanggalPanen, jumlahPanen, hargaPanen)
            val response = apiService.statusTanamClose(BEARER + token, requestBody)
            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun deleteTanam(token: String?, tanamId: String): Resource<WithoutDataResponseModel?> {
        return try {
            val response = apiService.deleteTanam(tanamId, BEARER + token)
            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getRiwayatTanam(
        token: String?,
        lahanId: String
    ): Resource<RiwayatTanamResponse?> {
        return try {
            val response = apiService.getRiwayatTanam(lahanId, BEARER + token)
            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    companion object {
        private const val BEARER = "Bearer "
    }
}