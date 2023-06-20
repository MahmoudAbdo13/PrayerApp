package com.example.prayerapp.RoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.prayerapp.data.Prayers

@Database(entities = [Entity::class], version = 1)
abstract class DatabaseClass : RoomDatabase() {
    abstract fun mDao(): Dao
    companion object
       {
           @Volatile
           private var Instance:DatabaseClass?=null

           fun getInstance(mCtx: Context): DatabaseClass {
                val temp= Instance
               if (temp !=null)
               {
                   return temp
               }
               synchronized(this)
               {
                   val instance=Room.databaseBuilder(
                       mCtx.applicationContext,
                       DatabaseClass::class.java,
                       "app_database"
                   ).allowMainThreadQueries()
                       .build()
                   Instance=instance
                   return instance
               }
           }
       }
}