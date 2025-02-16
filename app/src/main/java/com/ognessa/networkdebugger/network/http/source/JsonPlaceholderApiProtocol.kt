package com.ognessa.networkdebugger.network.http.source

import com.ognessa.networkdebugger.network.http.entity.PostModel
import com.ognessa.networkdebugger.network.http.entity.PostResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface JsonPlaceholderApiProtocol {

    @GET("/posts")
    @Headers("HEADER_value: Some text")
    suspend fun getPostsList(): Response<List<PostResponse>>

    @POST("/posts")
    @Headers("HEADER_value: Another text")
    suspend fun sendPost(
        @Body body: PostModel
    )

    @DELETE("/posts/{id}")
    @Headers("HEADER_value: Additional text")
    suspend fun deletePost(
        @Path("id") id: Int
    )
}