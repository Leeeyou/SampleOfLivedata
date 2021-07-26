package com.leeeyou123.samplelivedata.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.leeeyou123.samplelivedata.data.ArticleResponse
import com.leeeyou123.samplelivedata.util.RxUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WanAndroidService {

    @GET("article/list/0/json")
    suspend fun fetchArticles(): ArticleResponse

    companion object {
        private const val BASE_URL = "https://www.wanandroid.com/"

        fun create(): WanAndroidService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .sslSocketFactory(RxUtils.createSSLSocketFactory(), RxUtils.TrustAllManager())
                .hostnameVerifier(RxUtils.TrustAllHostnameVerifier())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
                .build()
                .create(WanAndroidService::class.java)
        }
    }
}
