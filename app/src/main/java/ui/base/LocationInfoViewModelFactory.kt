package ui.base

import ViewModels.WeatherViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import data.repository.DataRepository
import network.ApiHelper

class LocationInfoViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(DataRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

