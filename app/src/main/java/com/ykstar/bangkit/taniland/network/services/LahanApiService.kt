package com.ykstar.bangkit.taniland.network.services

import com.ykstar.bangkit.taniland.models.LahanRequest
import com.ykstar.bangkit.taniland.models.LahanResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LahanApiService {

    @GET("lahan")
    suspend fun getLahan(@Header("Authorization") token: String): Response<LahanResponse>

    @POST("lahan")
    suspend fun tambahLahan(
        @Header("Authorization") token: String,
        @Body lahan: LahanRequest
    ): Response<LahanResponse>
}
