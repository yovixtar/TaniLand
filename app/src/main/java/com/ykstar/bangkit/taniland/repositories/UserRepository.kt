package com.ykstar.bangkit.taniland.repositories

import com.ykstar.bangkit.taniland.models.AuthRequest
import com.ykstar.bangkit.taniland.models.AuthResponse
import com.ykstar.bangkit.taniland.models.UserModel
import com.ykstar.bangkit.taniland.network.ApiConfig
import com.ykstar.bangkit.taniland.network.services.UserApiService
import com.ykstar.bangkit.taniland.utils.Resource

class UserRepository {
    private val apiService = ApiConfig.createService(UserApiService::class.java)

    suspend fun authenticate(authdata: AuthRequest): Resource<AuthResponse> {
        return try {
            val response = apiService.authenticate(authdata)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getUserHome(token: String): Resource<UserModel> {
        return try {
            val response = apiService.getUser(BEARER + token)
            if (response.message == TOKEN_EXP) {
                Resource.Error(Exception(TOKEN_EXP))
            } else {
                Resource.Success(response.data)
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    companion object {
        private const val BEARER = "Bearer "
        private const val TOKEN_EXP = "Token expired"
    }
}