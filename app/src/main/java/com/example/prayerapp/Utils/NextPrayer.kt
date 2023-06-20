package com.example.prayerapp.Utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.prayerapp.data.PrayerTimes
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
fun getNextPrayerTitle(prayerTimes: List<PrayerTimes>) {
   /* val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)

    val currentTimeValue: LocalTime = LocalTime.parse(getCurrentTime(), timeFormatter)

    val fajr: LocalTime = LocalTime.parse(prayerTimes[0].prayerTime, timeFormatter)
    val sunrise: LocalTime = LocalTime.parse(prayerTimes[1].prayerTime, timeFormatter)
    val dhuhr: LocalTime = LocalTime.parse(prayerTimes[2].prayerTime, timeFormatter)
    val asr: LocalTime = LocalTime.parse(prayerTimes[3].prayerTime, timeFormatter)
    val maghrib: LocalTime = LocalTime.parse(prayerTimes[4].prayerTime, timeFormatter)
    val isha: LocalTime = LocalTime.parse(prayerTimes[5].prayerTime, timeFormatter)


    return if (currentTimeValue.isBefore(sunrise) && currentTimeValue.isAfter(fajr)) {
        //sunrise
        prayerTimes[1]
    } else if (currentTimeValue.isBefore(dhuhr) && currentTimeValue.isAfter(sunrise)) {
        //dhuhr
        prayerTimes[2]
    } else if (currentTimeValue.isBefore(asr) && currentTimeValue.isAfter(dhuhr)) {
        //asr
        prayerTimes[3]
    } else if (currentTimeValue.isBefore(maghrib) && currentTimeValue.isAfter(asr)) {
        //magribe
        prayerTimes[4]
    } else if (currentTimeValue.isBefore(isha) && currentTimeValue.isAfter(maghrib)) {
        //isha
        prayerTimes[5]
    } else {
        //fagr
        prayerTimes[0]
    }*/

}