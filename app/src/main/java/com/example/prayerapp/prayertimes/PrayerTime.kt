package com.example.prayerapp.prayertimes

import android.os.Build
import com.example.prayerapp.data.Timings
import java.text.ParseException
import java.text.SimpleDateFormat

class PrayerTime() {
    fun nextPrayer(timings: Timings):String
    {
        var currentTime:String=""
        try {
            var calendar: android.icu.util.Calendar? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                calendar = android.icu.util.Calendar.getInstance()
            }
            val hour = calendar!![android.icu.util.Calendar.HOUR_OF_DAY]
            val minute = calendar!![android.icu.util.Calendar.MINUTE]
            currentTime=hour.toString() + ":" + minute.toString()
        }catch (e:Exception)
        {

        }
        var hashMap=HashMap<String,String>()
        hashMap.set("Asr",timings.Asr)
        hashMap.set("Isha",timings.Isha)
        hashMap.set("Maghrib",timings.Maghrib)
        hashMap.set("Fajr",timings.Fajr)
        hashMap.set("Maghrib",timings.Maghrib)
        var less:Long=Long.MAX_VALUE
        var result="Maghrib"+":"+less
        hashMap.forEach{(key, value) ->
            var l=calculateDelay( currentTime,value)
            if (less>l && l>0)
            {
                result=key+":"+l
                less=l
            }
        }
        return result
    }
    fun calculateDelay(currentTime: String?, time: String?): Long {
        return try {
            val format = SimpleDateFormat("HH:mm")
            val date1 = format.parse(currentTime)
            val date2 = format.parse(time)
            date2.time - date1.time
        } catch (e: ParseException) {
            e.printStackTrace()
            -1
        }
    }
}