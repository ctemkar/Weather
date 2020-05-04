package com.ctemkar.weather.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ctemkar.weather.repository.DataRepository
import com.ctemkar.weather.network.ApiHelper

class LocationInfoViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(DataRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

