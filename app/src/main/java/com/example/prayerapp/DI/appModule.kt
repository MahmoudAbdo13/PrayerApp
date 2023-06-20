package com.example.prayerapp.DI
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class appModule :Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@appModule)
            modules(listOf(
                viewModule,
                repo,
                api,
                time,
                roomDatabase
            ))
        }
    }
}