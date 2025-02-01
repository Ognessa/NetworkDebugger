package com.ognessa.networkdebugger.network.retrofit

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ognessa.network_debugger.external.NetworkMonitorInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit

object NetworkProvider {

    private val contentType: MediaType by lazy { "application/json".toMediaType() }
    private val jsonConfiguration: Json by lazy { Json { ignoreUnknownKeys = true } }

    private val converterFactory: Converter.Factory by lazy {
        jsonConfiguration.asConverterFactory(
            contentType
        )
    }

    fun getJson(): Json = jsonConfiguration

    fun <T> provideApiService(retrofit: Retrofit, service: Class<T>): T = retrofit.create(service)

    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }

    fun provideOkHttp(
        context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(NetworkMonitorInterceptor(context))
            .retryOnConnectionFailure(true)
            .build()
    }

}