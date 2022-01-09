package com.sgz.ghsearch.api.mocks

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sgz.ghsearch.api.GithubApi
import com.sgz.ghsearch.utilities.jsonDefaultInstance
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import java.net.URL

private const val mockURLHost = "mock.url"
private const val RESOURCES_PATH = "src/test/res/com/sgz/ghsearch/api/mocks"

private fun URL.toMockURL(): URL {

    return URL("http", mockURLHost, file)
}

private fun URL.toFileURL(): URL {

    return URL("file", "", RESOURCES_PATH + file)
}

private fun HttpUrl.toFileURL(): URL {

    return toUrl().toFileURL()
}

private val HttpUrl.isMockURL: Boolean get() = host == mockURLHost

fun HttpUrl.removingQueryParameters(): HttpUrl {
    val builder = newBuilder()
    for (param in queryParameterNames) {
        builder.removeAllQueryParameters(param)
    }

    return builder.build()
}

val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
    val request = chain.request()

    if (!request.url.isMockURL) {

        return@addInterceptor chain.proceed(request)
    }

    val body = request.url.removingQueryParameters().toFileURL().readText()
        .toResponseBody("application/json".toMediaTypeOrNull())
    return@addInterceptor Response.Builder().body(body).code(200).request(request)
        .protocol(Protocol.HTTP_1_1).message("OK").build()
}.build()

val baseURL = object {}.javaClass.getResource("/")!!.toMockURL()

@ExperimentalSerializationApi
val mockedGithubApi: GithubApi = Retrofit.Builder()
    .addConverterFactory(jsonDefaultInstance.asConverterFactory("application/json".toMediaType()))
    .baseUrl(baseURL)
    .client(okHttpClient)
    .build()
    .create(GithubApi::class.java)