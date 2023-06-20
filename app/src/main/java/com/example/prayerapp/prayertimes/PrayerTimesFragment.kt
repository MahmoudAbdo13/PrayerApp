package com.example.prayerapp.prayertimes

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prayerapp.MVVM.TimesViewModel
import com.example.prayerapp.NotifyDataChange
import com.example.prayerapp.RoomDatabase.Entity
import com.example.prayerapp.SharedPreferences
import com.example.prayerapp.data.Data
import com.example.prayerapp.data.PrayerTimes
import com.example.prayerapp.data.Timings
import com.example.prayerapp.databinding.FragmentPrayerTimesBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

class PrayerTimesFragment : Fragment() , NotifyDataChange{

    private lateinit var binding: FragmentPrayerTimesBinding
    private val mViewModel: TimesViewModel by sharedViewModel()
    private var prayTimes= ArrayList<PrayerTimes>()
    private lateinit var listDays: ArrayList<Data>
    private  var rListDays= ArrayList<Entity>()

    val PrayerTime: PrayerTime by inject()
    private val adapter: PrayerTimesAdapter by lazy {
        PrayerTimesAdapter(prayTimes)
    }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val TAG = "MainActivity"
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var altitude: Double = 0.0
    private var dayNumber: Int = 0
    private var room=false
    override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = inflater.let { FragmentPrayerTimesBinding.inflate(it, container, false) }
        return binding.root
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dayOfMonth()
        recyclerViewData()
        nextDay()
        previousDay()
        showQibla(view)
        if (isInternetConnected(requireContext())) {
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
            getCurrentLocation()
        }
        else
        {
            room=true
            getDataFromRoom()
            longitude=SharedPreferences.getLongitude(requireContext()).toString().toDouble()
            latitude=SharedPreferences.getLatitude(requireContext()).toString().toDouble()

        }
    }
    private fun getDataFromRoom()
    {
        mViewModel.getRoomData().observe(viewLifecycleOwner) {
            rListDays=it
            addDataToView()
            binding.prayerProgressbar.visibility = View.GONE
            binding.prayersList.adapter = adapter
        }
    }

    private fun showQibla(view: View)
    {
        binding.showOnMapBtn.setOnClickListener {
            val action = PrayerTimesFragmentDirections.actionPrayerTimesFragmentToQiblaFragment(
                "currentAddres",
                "getSystemDate",
                latitude.toFloat(), longitude.toFloat(),
                altitude.toFloat()
            )
            Navigation.findNavController(view).navigate(action)
        }
    }
    @SuppressLint("SimpleDateFormat")
    fun getPrayerTimes(longitude: String ,latitude: String) {
        mViewModel.setApiData(this,longitude,latitude)
    }
    fun getAddressGeocoder(context: Context, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address: Address?

        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null) {
            address = addresses[0]
            //   val address = "$add, $state, $country"
            val city = address?.locality
            val state = address?.adminArea
            val country = address?.countryName

            return "$city, $state, $country"
        }
        return null
    }

    @SuppressLint("SetTextI18n")
    fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location = task.result
                    if (location.latitude.equals(null)) {
                        Log.e(TAG, "Location not provided, try again: " + location.latitude)

                        Toast.makeText(
                            requireContext(),
                            "Location not provided, try again",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(context, "Get location successfully", Toast.LENGTH_LONG)
                            .show()
                        latitude = location.latitude
                        longitude = location.longitude
                        altitude = location.altitude
                        Log.e(TAG, "latitude: " + location.latitude)
                        Log.e(TAG, "longitude: " + location.longitude)
                        Log.e(TAG, "altitude: " + location.altitude)
                        getPrayerTimes(longitude.toString(),latitude.toString())
                        SharedPreferences.setLongitude(requireContext(),longitude.toString())
                        SharedPreferences.setLatitude(requireContext(),latitude.toString())

                        binding.address.text = getAddressGeocoder(requireContext(), latitude, longitude)
                    }
                }
            } else {
                Log.e(TAG, "Turn on Location")
                Toast.makeText(requireContext(), "Turn on Location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    companion object {
        const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Granted", Toast.LENGTH_LONG).show()
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Denied", Toast.LENGTH_LONG).show()
                closeApplication()
            }
        }
    }

    private fun closeApplication() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
        exitProcess(0)
    }

    private fun recyclerViewData() {
        binding.prayerProgressbar?.visibility = View.VISIBLE
        binding.prayersList.apply {
            layoutManager=LinearLayoutManager(activity)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun dayOfMonth() {
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd-MM-yyyy")
        val saveCurrentDate = currentDate.format(calendar.time)
        val dayOfMonth = saveCurrentDate.substring(0, 2).toInt()
        dayNumber = dayOfMonth - 1
    }


    private fun nextDay() {
        binding.next.setOnClickListener()
        {
            dayNumber++
            addDataToView()
        }
    }

    private fun previousDay() {
        binding.back.setOnClickListener()
        {
            dayNumber--
            addDataToView()
        }
    }

    private fun addDataToView() {

        prayTimes.clear()
        if (!room) {
            val timings: Timings = listDays.get(dayNumber).timings
            val day: String = listDays.get(dayNumber).date.hijri.weekday.ar
            val date: String = listDays.get(dayNumber).date.readable
            prayTimes.addAll(convertFromTimings(timings))
            binding.date.text = date
            //binding?.?.text = day
            adapter.notifyDataSetChanged()
            getNextPrayer(timings)
        }
        else
        {
            var time=Timings(rListDays.get(dayNumber).Asr,rListDays.get(dayNumber).Dhuhr,
                rListDays.get(dayNumber).Fajr,rListDays.get(dayNumber).Firstthird,rListDays.get(dayNumber).Imsak,
                rListDays.get(dayNumber).Isha,rListDays.get(dayNumber).Lastthird,rListDays.get(dayNumber).Maghrib,
                rListDays.get(dayNumber).Midnight,rListDays.get(dayNumber).Sunrise,rListDays.get(dayNumber).Sunset)
            prayTimes.addAll(convertFromTimings(time))
            binding.date.text = rListDays.get(dayNumber).readable
            //binding?.?.text = day
            adapter.notifyDataSetChanged()
            getNextPrayer(time)
        }
        binding.layout.visibility=View.VISIBLE

    }
    private fun convertFromTimings(timings: Timings): ArrayList<PrayerTimes> {
        val res: ArrayList<PrayerTimes> = ArrayList<PrayerTimes>()
        res.add(PrayerTimes("Fajr", timings.Fajr.substring(0, 6)))
        res.add(PrayerTimes("Dhuhr", timings.Dhuhr.substring(0, 6)))
        res.add(PrayerTimes("Asr", timings.Asr.substring(0, 6)))
        res.add(PrayerTimes("Maghrib", timings.Maghrib.substring(0, 6)))
        res.add(PrayerTimes("Isha", timings.Isha.substring(0, 6)))
        return res
    }
    override fun onDataChange() {
        mViewModel.getApiData().observe(viewLifecycleOwner) {
            listDays = it
            addDataToView()
            binding.prayerProgressbar.visibility = View.GONE
            binding.prayersList.adapter = adapter
        }
    }
    private fun getNextPrayer(timings: Timings)
    {

            var kl=PrayerTime.nextPrayer(timings)
            var l=kl.split(":")
            binding.prayerName.text=l.get(0)
            var milli=l.get(1).toInt()
            val totalSeconds = milli / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            binding.timeLeft.text="time left\n"+hours.toString() +" hr "+minutes+" min"

    }
    fun isInternetConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
