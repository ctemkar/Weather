package ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import data.repository.DataRepository
import main.MainViewModel
import network.ApiHelper

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(DataRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}
