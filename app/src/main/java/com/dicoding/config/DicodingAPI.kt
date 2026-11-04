package com.dicoding.config

import com.dicoding.dto.DicodingDetailEvent
import com.dicoding.dto.DicodingListEvent
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface DicodingAPI {
    @GET("events?active=1")
    fun getActiveEvent(): Call<DicodingListEvent>

    @GET("events?active=0")
    fun getFinishEvent(): Call<DicodingListEvent>

    @GET("events/{id}")
    fun getDetail(@Path("id") id: Int): Call<DicodingDetailEvent>

    @GET("events")
    fun getSearchEvent(
        @Query("q") query: String,
        @Query("active") active: Int
    ): Call<DicodingListEvent>

    companion object {
        private const val BASE_URL = "https://event-api.dicoding.dev/"
        private val clientConfig: OkHttpClient = OkHttpClient().newBuilder()
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .build()

        private val gsonConfig = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        private fun getInstance(): Retrofit {
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(
                    GsonConverterFactory.create(gsonConfig)
                )
                .client(clientConfig)
                .build()
        }

        val client: DicodingAPI by lazy {
            getInstance().create(DicodingAPI::class.java)
        }
    }
}