package com.example.prayerapp.RoomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "azan")
class Entity (var Asr: String,
              var Dhuhr: String,
              var Fajr: String,
              var Firstthird: String,
              var Imsak: String,
              var Isha: String,
              var Lastthird: String,
              var Maghrib: String,
              var Midnight: String,
              var Sunrise: String,
              var Sunset: String,
              var readable: String,
              var timestamp: String,
              var day: String){
    @PrimaryKey(
        autoGenerate = true
    )
    var id : Int ?=null
}