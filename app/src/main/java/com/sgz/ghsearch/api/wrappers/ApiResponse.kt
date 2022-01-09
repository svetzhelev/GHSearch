package com.sgz.ghsearch.api.wrappers

import com.sgz.ghsearch.models.ApiError
import com.sgz.ghsearch.utilities.jsonDefaultInstance
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.Call

class ApiResponse<T>(call: Call<T>) {

    var data: T? = null
    var code: Int = -1
    var success = code in 200..300
    var headers: Headers? = null

    init {
        call.execute().apply {
            code = code()
            data = body()
            success = code in 200..300
            headers = headers()
            parseError(errorBody())?.let { error -> throw Throwable(error) }
        }

    }

    private fun parseError(errorBody: ResponseBody?): String? {
        return errorBody?.let { body ->
            jsonDefaultInstance.decodeFromString(ApiError.serializer(), body.string()).message
        }
    }
}