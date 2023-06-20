package com.example.prayerapp.MVVM

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.prayerapp.NotifyDataChange
import com.example.prayerapp.RoomDatabase.DatabaseClass
import com.example.prayerapp.RoomDatabase.Entity
import com.example.prayerapp.data.Data
import com.example.prayerapp.data.Prayers
import com.example.prayerapp.network.ApiClient
import com.example.prayerapp.network.PrayerInterface
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar

class TimesRepo (private val api:PrayerInterface,private val mDatabase: DatabaseClass){
    private  var listDays= ArrayList<Data>()
    //private lateinit var mDatabase: DatabaseClass
    private lateinit var notifyDataChange:NotifyDataChange
    private var longitude:String?=null
    private var latitude:String?=null
    fun initializeRepo(fragment:Fragment, longitude:String,latitude:String)
    {
        this.latitude=latitude
        this.longitude=longitude
        //mDatabase = DatabaseClass.getInstance(fragment.requireActivity().applicationContext)
        notifyDataChange=  fragment as NotifyDataChange
    }
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("SimpleDateFormat")
    fun data() {
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd-MM-yyyy")
        val saveCurrentDate = currentDate.format(calendar.time)
        val dayOfMonth = saveCurrentDate.substring(0, 2).toInt()
        val month = saveCurrentDate.substring(3, 5).toInt()
        val year = saveCurrentDate.substring(6).toInt()
        Log.e("TAG", "onViewCreated: $saveCurrentDate")
        Log.e("day", "onViewCreated: $dayOfMonth")
        Log.e("month", "onViewCreated: $month")
        Log.e("year", "onViewCreated: $year")

        GlobalScope.launch {
            val prayers: Prayers? = api.getPrayers(
                latitude,
                longitude,
                4,
                month,
                year
            )
            if (prayers != null) {
                listDays .addAll( prayers.data)

                withContext(Dispatchers.Main) {
                    for (d in prayers.data) {
                        addToRoom(
                            Entity(
                                d.timings.Asr,
                                d.timings.Dhuhr,
                                d.timings.Fajr,
                                d.timings.Firstthird,
                                d.timings.Imsak,
                                d.timings.Isha,
                                d.timings.Lastthird,
                                d.timings.Maghrib,
                                d.timings.Midnight,
                                d.timings.Sunrise,
                                d.timings.Sunset,
                                d.date.readable,
                                d.date.timestamp,
                                d.date.hijri.weekday.ar
                            )
                        )
                    }
                    notifyDataChange.onDataChange()
                }
            }
        }
    }
    private fun addToRoom(p: Entity) {
        mDatabase.mDao().insertNews(p);
    }
    fun returnApiData():MutableLiveData< ArrayList<Data>>
    {
        listDays.clear()
        data()
        var mutableList=MutableLiveData< ArrayList<Data>>()
        mutableList.value=listDays
        return mutableList
    }
    fun returnRoomData():MutableLiveData< ArrayList<Entity>>
    {
        var arr=ArrayList<Entity>()
        var t= mDatabase.mDao().getlist()
        for (k in t) {
            Log.e("tagRoom", k.day);
            Log.e("tagRoom2", k.readable);
        }
        arr.addAll(t)
        var mutableList=MutableLiveData< ArrayList<Entity>>()
        mutableList.value=arr
        return mutableList
    }
}