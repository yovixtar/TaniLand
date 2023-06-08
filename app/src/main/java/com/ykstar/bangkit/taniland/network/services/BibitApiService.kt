package com.ykstar.bangkit.taniland.network.services

import com.ykstar.bangkit.taniland.models.BibitResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface BibitApiService {
    @GET("bibit")
    suspend fun getBibit(@Header("Authorization") token: String): Response<BibitResponse>
}
