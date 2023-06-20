package com.example.prayerapp.prayertimes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prayerapp.R
import com.example.prayerapp.data.PrayerTimes

class PrayerTimesAdapter (private var timing: List<PrayerTimes>): RecyclerView.Adapter<PrayerTimesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_times, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(timing!![position])
    }

    override fun getItemCount(): Int {
        return timing!!.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var prayerName: TextView
        var prayerTime: TextView

        init {
            prayerName = itemView.findViewById(R.id.prayer_name)
            prayerTime = itemView.findViewById(R.id.prayer_time)
        }

        fun bind(timing: PrayerTimes) {
            prayerName.text = timing.prayerName
            prayerTime.text = timing.prayerTime
        }
    }
}