package com.example.prayerapp.network

import androidx.viewbinding.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {
        private var instance: Retrofit? = null

        //http://api.aladhan.com/v1/calendar?latitude=51.508515&longitude=-0.1254872&method=2&month=4&year=2017
        private val BASE_URL = "http://api.aladhan.com/v1/"
        private var api: PrayerInterface? = null
        private fun getInstance(): Retrofit? {
            if (instance == null) {

                val logging = HttpLoggingInterceptor()
                logging.setLevel(
                    if (BuildConfig.DEBUG)
                        HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                )
                val client = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(8, TimeUnit.SECONDS)
                    .writeTimeout(8, TimeUnit.SECONDS)
                    .build()
                val gson = GsonBuilder().setLenient().create()
                instance = Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return instance
        }

        fun getAPI(): PrayerInterface? {
            if (api == null) {
                api = getInstance()?.create(PrayerInterface::class.java)
            }
            return api
        }
    }
}