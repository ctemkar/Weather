package com.ctemkar.weather

import android.app.Application


@SuppressWarnings("all")
class WeatherApp : Application() {
    companion object {
        lateinit var instance: WeatherApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
//            Stetho.initializeWithDefaults(this)
//            DatabaseCreator.createDatabase(this)
    }
}
