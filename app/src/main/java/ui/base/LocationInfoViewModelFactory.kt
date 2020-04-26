package ui.base

import ViewModels.LocationInfoViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import data.repository.DataRepository
import network.ApiHelper

class LocationInfoViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationInfoViewModel::class.java)) {
            return LocationInfoViewModel(DataRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

