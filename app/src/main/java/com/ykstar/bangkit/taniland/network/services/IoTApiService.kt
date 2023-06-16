package com.ykstar.bangkit.taniland.network.services

import com.ykstar.bangkit.taniland.models.HasilIoTResponse
import com.ykstar.bangkit.taniland.models.RegistIoTRequest
import com.ykstar.bangkit.taniland.models.WithoutDataResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface IoTApiService {

    @POST("iot")
    suspend fun registIoT(
        @Header("Authorization") token: String,
        @Body request: RegistIoTRequest,
    ): Response<WithoutDataResponseModel>

    @GET("iot/{id}")
    suspend fun getHasilIoT(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<HasilIoTResponse?>

    @POST("iot/reset")
    suspend fun resetIoT(
        @Header("Authorization") token: String,
        @Body request: Map<String, String>,
    ): Response<WithoutDataResponseModel>

}
