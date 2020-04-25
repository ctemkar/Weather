package main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import data.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import utils.Resource

class MainViewModel(private val mainRepository: DataRepository) : ViewModel() {

    fun getUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getWeather()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}