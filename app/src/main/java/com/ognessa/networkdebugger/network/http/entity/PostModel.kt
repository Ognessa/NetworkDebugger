package com.ognessa.networkdebugger.network.http.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PostModel(
    @SerialName("userId") val userId: Int,
    @SerialName("title") val title: String,
    @SerialName("body") val body: String
)