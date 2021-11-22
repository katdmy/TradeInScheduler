package ru.katdmy.tradein.domain

import ru.katdmy.tradein.domain.retrofit.SmkhApi
import ru.katdmy.tradein.model.AdListResponse

class MainRepository(
    private val smkhApi: SmkhApi
) {
    //suspend fun loadAds(): AdListResponse = smkhApi.getAdList(AdListRequest("KATDMY"))
    suspend fun loadAds(): AdListResponse = smkhApi.getAdList(mapOf("login" to "KATDMY"))
}