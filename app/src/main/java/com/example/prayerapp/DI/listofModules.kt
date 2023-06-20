package com.example.prayerapp.DI
import android.app.Application
import android.content.Context
import com.example.prayerapp.MVVM.TimesRepo
import com.example.prayerapp.MVVM.TimesViewModel
import com.example.prayerapp.RoomDatabase.DatabaseClass
import com.example.prayerapp.network.ApiClient
import com.example.prayerapp.prayertimes.PrayerTime
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModule: Module = module {
   viewModel{ TimesViewModel(repo=get())}
}
val repo:Module= module {
    single { TimesRepo(api=get(),mDatabase=get()) }
}

val api:Module= module {
    single { ApiClient.getAPI() }
}
val time:Module= module {
    single { PrayerTime() }
}
val roomDatabase:Module= module {
    single { DatabaseClass.getInstance(androidContext()) }
}

