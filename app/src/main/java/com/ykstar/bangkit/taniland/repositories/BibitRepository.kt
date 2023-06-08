package com.ykstar.bangkit.taniland.repositories

import com.ykstar.bangkit.taniland.models.BibitResponse
import com.ykstar.bangkit.taniland.network.ApiConfig
import com.ykstar.bangkit.taniland.network.services.BibitApiService
import com.ykstar.bangkit.taniland.utils.Resource

class BibitRepository {
    private val apiService = ApiConfig.createService(BibitApiService::class.java)

    suspend fun getBibit(token: String): Resource<BibitResponse?> {
        return try {
            val response = apiService.getBibit(BEARER + token)
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