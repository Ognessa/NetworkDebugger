package com.ognessa.networkdebugger.network.http.repository

interface JsonPlaceholderRepository {

    suspend fun getPostsList()

    suspend fun sendPost()

    suspend fun deletePost()
}