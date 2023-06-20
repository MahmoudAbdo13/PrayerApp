package com.example.prayerapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.prayerapp.R
import com.example.prayerapp.data.PrayerTimes
import com.example.prayerapp.data.Prayers
import com.example.prayerapp.data.Timings
import com.example.prayerapp.databinding.FragmentPrayerTimesBinding
import com.example.prayerapp.network.ApiClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.splash_screen, container, false)
    }
}
