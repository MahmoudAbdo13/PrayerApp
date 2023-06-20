package com.example.prayerapp.network

import com.example.prayerapp.data.Prayers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PrayerInterface {
    @GET("calendar")
    suspend fun getPrayers(
        @Query("latitude") latitude: String?,
        @Query("longitude") longitude: String?,
        @Query("method") method: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Prayers?
}