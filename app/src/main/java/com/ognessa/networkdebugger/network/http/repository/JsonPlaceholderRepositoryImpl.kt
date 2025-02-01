package com.ognessa.networkdebugger.network.http.repository

import com.ognessa.networkdebugger.network.http.entity.PostModel
import com.ognessa.networkdebugger.network.http.source.JsonPlaceholderApiProtocol

class JsonPlaceholderRepositoryImpl(
    private val protocol: JsonPlaceholderApiProtocol
) : JsonPlaceholderRepository {

    override suspend fun getPostsList() {
        try {
            protocol.getPostsList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun sendPost() {
        try {
            protocol.sendPost(
                PostModel(
                    userId = 1,
                    title = "Some title",
                    body = "Some body"
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deletePost() {
        try {
            protocol.deletePost(id = 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}