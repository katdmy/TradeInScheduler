package ru.katdmy.tradein.model

import Ad
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdListResponse (
    @SerialName("status")
    val status: String? = null,
    @SerialName("data")
    val data: List<Ad>? = null,
    @SerialName("description")
    val descriprion: String? = null
)