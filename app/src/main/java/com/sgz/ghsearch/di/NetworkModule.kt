package com.sgz.ghsearch.di

import android.app.Application
import com.sgz.ghsearch.Config
import com.sgz.ghsearch.api.GithubApi
import com.sgz.ghsearch.networking.AuthorizationInterceptor
import com.sgz.ghsearch.utilities.jsonDefaultInstance
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object{
        private const val CACHE_SIZE = 10L * 1024 * 1024 // 10MB
        private const val TIMEOUT = 30L // Seconds
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        application: Application
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.cache(Cache(application.cacheDir, CACHE_SIZE))
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS).connectTimeout(TIMEOUT, TimeUnit.SECONDS)

        builder.interceptors().add(AuthorizationInterceptor())

        return builder.build()
    }

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideGitHubApi(okHttpClient: OkHttpClient): GithubApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .addConverterFactory(jsonDefaultInstance.asConverterFactory(contentType))
            .baseUrl(Config.BASE_URL)
            .client(okHttpClient)
            .build()
            .create(GithubApi::class.java)
    }
}

