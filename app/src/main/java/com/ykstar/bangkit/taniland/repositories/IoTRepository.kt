package com.ykstar.bangkit.taniland.repositories

import com.ykstar.bangkit.taniland.models.HasilIoTResponse
import com.ykstar.bangkit.taniland.models.RegistIoTRequest
import com.ykstar.bangkit.taniland.models.WithoutDataResponseModel
import com.ykstar.bangkit.taniland.network.ApiConfig
import com.ykstar.bangkit.taniland.network.services.IoTApiService
import com.ykstar.bangkit.taniland.utils.Resource

class IoTRepository {
    private val apiService = ApiConfig.createService(IoTApiService::class.java)

    suspend fun regitIoT(
        token: String?,
        iotId: String,
        lahanId: String,
    ): Resource<WithoutDataResponseModel?> {
        return try {
            val requestBody = RegistIoTRequest(iotId, lahanId)
            val response = apiService.registIoT(BEARER + token, requestBody)
            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getHasilIoT(
        iotId: String,
        token: String?,
    ): Resource<HasilIoTResponse?> {
        return try {
            val response = apiService.getHasilIoT(iotId, BEARER + token)
            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun resetIoT(
        token: String?,
        iotId: String,
    ): Resource<WithoutDataResponseModel?> {
        return try {
            val response = apiService.resetIoT(BEARER + token, mapOf("id" to iotId))
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