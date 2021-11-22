package ru.katdmy.tradein.domain.retrofit

import retrofit2.http.Body
import retrofit2.http.POST
import ru.katdmy.tradein.model.AdListResponse
import ru.katdmy.tradein.model.AdListRequest

interface SmkhApi {

    @POST("auth")
    suspend fun auth(
        @Body login: String,
        @Body pwd: String
    ): String

    @POST("adlist")
    //suspend fun getAdList(@Body login: AdListRequest): AdListResponse
    suspend fun getAdList(@Body login: Map<String, String>): AdListResponse
}