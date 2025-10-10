package com.discdogs.app

import android.app.Application
import android.content.Context
import com.discdogs.app.di.initKoin
import org.koin.android.ext.koin.androidContext

class DiscDogsApplication : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        initKoin {
            androidContext(this@DiscDogsApplication)
        }
    }
}