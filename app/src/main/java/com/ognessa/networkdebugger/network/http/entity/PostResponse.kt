package com.ognessa.networkdebugger.network.http.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PostResponse(
    @SerialName("userId") val userId: Int? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("body") val body: String? = null
)