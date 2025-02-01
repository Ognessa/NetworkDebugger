package com.ognessa.networkdebugger.di

import android.content.Context
import com.ognessa.networkdebugger.network.http.repository.JsonPlaceholderRepository
import com.ognessa.networkdebugger.network.http.repository.JsonPlaceholderRepositoryImpl
import com.ognessa.networkdebugger.network.http.source.JsonPlaceholderApiProtocol
import com.ognessa.networkdebugger.network.retrofit.NetworkProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideJson(): Json {
        return NetworkProvider.getJson()
    }

    @Provides
    fun provideOkHttp(
        @ApplicationContext context: Context
    ): OkHttpClient {
        return NetworkProvider.provideOkHttp(
            context = context
        )
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return NetworkProvider.provideRetrofit(
            okHttpClient = okHttpClient,
            baseUrl = "https://jsonplaceholder.typicode.com"
        )
    }

    @Provides
    fun provideJsonPlaceholderApiProtocol(
        retrofit: Retrofit
    ): JsonPlaceholderApiProtocol {
        return NetworkProvider.provideApiService(
            retrofit = retrofit,
            service = JsonPlaceholderApiProtocol::class.java
        )
    }

    @Provides
    fun provideJsonPlaceholderRepository(
        protocol: JsonPlaceholderApiProtocol
    ): JsonPlaceholderRepository {
        return JsonPlaceholderRepositoryImpl(
            protocol = protocol
        )
    }
}