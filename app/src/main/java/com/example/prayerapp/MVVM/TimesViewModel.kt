package com.example.prayerapp.MVVM

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.prayerapp.RoomDatabase.Entity
import com.example.prayerapp.data.Data
import kotlin.text.Typography.times

class TimesViewModel (private val repo:TimesRepo) :ViewModel() {
    private var mApiData= MutableLiveData<ArrayList<Data>>()
    private var mRoomData= MutableLiveData<ArrayList<Entity>>()

    fun setApiData(mFragment: Fragment, longitude:String,latitude:String)
    {
        repo.initializeRepo(mFragment, longitude,latitude)
        mApiData=repo.returnApiData()
        Log.d("error", "er4")
    }
    fun getApiData(): MutableLiveData<ArrayList<Data>> {
        return mApiData
    }
    fun getRoomData(): MutableLiveData<ArrayList<Entity>> {
        mRoomData=repo.returnRoomData()
        return mRoomData
    }

}