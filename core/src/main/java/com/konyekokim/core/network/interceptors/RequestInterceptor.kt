package com.konyekokim.core.network.interceptors

import com.konyekokim.core.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RequestInterceptor @Inject constructor(): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val httpUrl = request.url.newBuilder()
            .addQueryParameter(QUERY_NAME_API_KEY, BuildConfig.API_KEY)
            .addQueryParameter(QUERY_NAME_API_UNITS, BuildConfig.API_UNITS)
            .build()

        request = request.newBuilder().url(httpUrl).build()

        return chain.proceed(request)
    }

    companion object {
        const val QUERY_NAME_API_KEY = "appid"
        const val QUERY_NAME_API_UNITS = "units"
    }

}