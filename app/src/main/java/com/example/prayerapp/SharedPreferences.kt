package com.example.prayerapp

import android.content.Context

class SharedPreferences {
    companion object {
        fun getLongitude(context: Context): String? {
            val sharedpreferences =
                context.getSharedPreferences("longitude", Context.MODE_PRIVATE)
            return sharedpreferences.getString("longitude", " ")
        }

        fun setLongitude(context: Context, token: String?) {
            val sharedpreferences =
                context.getSharedPreferences("longitude", Context.MODE_PRIVATE)
            val editor = sharedpreferences.edit()
            editor.putString("longitude", token)
            editor.apply()
        }
        fun getLatitude(context: Context): String? {
            val sharedpreferences =
                context.getSharedPreferences("latitude", Context.MODE_PRIVATE)
            return sharedpreferences.getString("latitude", " ")
        }

        fun setLatitude(context: Context, token: String?) {
            val sharedpreferences =
                context.getSharedPreferences("latitude", Context.MODE_PRIVATE)
            val editor = sharedpreferences.edit()
            editor.putString("latitude", token)
            editor.apply()
        }
    }
}