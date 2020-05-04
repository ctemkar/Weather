package com.ctemkar.weather.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ctemkar.weather.database.getDatabase
import com.ctemkar.weather.repository.DataRepository
import network.ApiHelper

class LocationInfoViewModelFactory(private val apiHelper: ApiHelper, private val context: Context?) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            val db = getDatabase(context)
            return context?.let {
                WeatherViewModel(DataRepository(apiHelper, db), it)
            } as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

