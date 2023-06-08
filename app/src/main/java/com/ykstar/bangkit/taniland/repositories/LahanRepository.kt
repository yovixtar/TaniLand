package com.ykstar.bangkit.taniland.repositories

import com.ykstar.bangkit.taniland.models.DetailLahanResponse
import com.ykstar.bangkit.taniland.models.LahanRequest
import com.ykstar.bangkit.taniland.models.LahanResponse
import com.ykstar.bangkit.taniland.network.ApiConfig
import com.ykstar.bangkit.taniland.network.services.LahanApiService
import com.ykstar.bangkit.taniland.utils.Resource

class LahanRepository {
    private val apiService = ApiConfig.createService(LahanApiService::class.java)

    suspend fun getLahan(token: String): Resource<LahanResponse?> {
        return try {
            val response = apiService.getLahan(BEARER + token)
            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun tambahLahan(token: String?, lahan: LahanRequest): Resource<LahanResponse?> {
        return try {
            val response = apiService.tambahLahan(BEARER + token, lahan)
            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getLahanDetail(id: String, token: String): Resource<DetailLahanResponse?> {
        return try {
            val response = apiService.getLahanDetail(id, BEARER + token)
            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(Exception(e.message))
        }
    }

    companion object {
        private const val BEARER = "Bearer "
    }
}
