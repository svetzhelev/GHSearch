package com.sgz.ghsearch.networking

import com.sgz.ghsearch.Config
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Singleton

@Singleton
class AuthorizationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.header("Accept", "application/json")
        builder.header("Authorization", "Basic ${Config.TOKEN}")

        return chain.proceed(builder.build())
    }
}