package com.ykstar.bangkit.taniland.network.services

import com.ykstar.bangkit.taniland.models.AuthResponse
import com.ykstar.bangkit.taniland.models.UserHomeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApiService {
    @POST("auth")
    suspend fun authenticate(@Body body: Map<String?, String?>): AuthResponse

    @GET("user")
    suspend fun getUser(@Header("Authorization") authHeader: String): UserHomeResponse
}
