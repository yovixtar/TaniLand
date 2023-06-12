package com.ykstar.bangkit.taniland.network.services

import com.ykstar.bangkit.taniland.models.DetailLahanResponse
import com.ykstar.bangkit.taniland.models.LahanRequest
import com.ykstar.bangkit.taniland.models.LahanResponse
import com.ykstar.bangkit.taniland.models.WithoutDataResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface LahanApiService {

    @GET("lahan")
    suspend fun getLahan(@Header("Authorization") token: String): Response<LahanResponse>

    @POST("lahan")
    suspend fun tambahLahan(
        @Header("Authorization") token: String,
        @Body lahan: LahanRequest
    ): Response<LahanResponse>

    @GET("lahan/{id}")
    suspend fun getLahanDetail(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<DetailLahanResponse?>

    @DELETE("lahan/{id}")
    suspend fun deleteLahan(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<WithoutDataResponseModel?>
}
