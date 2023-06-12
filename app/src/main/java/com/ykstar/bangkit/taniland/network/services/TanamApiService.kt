package com.ykstar.bangkit.taniland.network.services

import com.ykstar.bangkit.taniland.models.RiwayatTanamResponse
import com.ykstar.bangkit.taniland.models.StatusTanamCloseRequest
import com.ykstar.bangkit.taniland.models.StatusTanamExecRequest
import com.ykstar.bangkit.taniland.models.StatusTanamPlanRequest
import com.ykstar.bangkit.taniland.models.WithoutDataResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TanamApiService {

    @POST("tanam/plan")
    suspend fun statusTanamPlan(
        @Header("Authorization") token: String,
        @Body request: StatusTanamPlanRequest,
    ): Response<WithoutDataResponseModel>

    @POST("tanam/exec")
    suspend fun statusTanamExec(
        @Header("Authorization") token: String,
        @Body request: StatusTanamExecRequest,
    ): Response<WithoutDataResponseModel>

    @POST("tanam/close")
    suspend fun statusTanamClose(
        @Header("Authorization") token: String,
        @Body request: StatusTanamCloseRequest,
    ): Response<WithoutDataResponseModel>

    @DELETE("tanam/{id}")
    suspend fun deleteTanam(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<WithoutDataResponseModel?>

    @GET("tanam/close")
    suspend fun getRiwayatTanam(
        @Query("lahan_id") lahanId: String,
        @Header("Authorization") token: String,
    ): Response<RiwayatTanamResponse>
}