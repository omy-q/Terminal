package com.example.terminal.data

import com.example.terminal.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

internal object AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder().apply {
            addHeader(AUTH_HEADER_NAME, "Bearer ${BuildConfig.DEFAULT_API_KEY}")
        }
        return chain.proceed(builder.build())
    }

    private const val AUTH_HEADER_NAME = "Authorization"
}
