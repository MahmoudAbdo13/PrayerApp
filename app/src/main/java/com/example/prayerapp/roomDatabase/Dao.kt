package com.example.prayerapp.RoomDatabase

import androidx.room.*
import androidx.room.Dao
import com.example.prayerapp.data.Prayers

@Dao
interface Dao {

    @Query("select * from azan")
    fun getlist():List<Entity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insertNews(entity: Entity)

    @Query("DELETE FROM azan")
    fun deleteAll()
}