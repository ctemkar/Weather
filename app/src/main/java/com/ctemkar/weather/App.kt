package com.ctemkar.weather

import android.app.Application


@SuppressWarnings("all")
class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
//            Stetho.initializeWithDefaults(this)
//            DatabaseCreator.createDatabase(this)
    }
}
