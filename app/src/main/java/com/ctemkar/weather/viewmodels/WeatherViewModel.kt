package com.ctemkar.weather.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.soywiz.klock.DateTime
import com.ctemkar.weather.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import utils.Resource

class WeatherViewModel(private val dataRepository: DataRepository, private val context: Context) : ViewModel()
 {
// class WeatherViewModel(application: Application) : AndroidViewModel(application) {

   // private val dataRepository = DataRepository(getDatabase(application))

    fun getLocationInfo(latlong: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dataRepository.getLocationInfo(latlong)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
    fun getCurrentWeather(woeId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dataRepository.getCurrentWeather(woeId)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
    fun getWeatherDateRange(woeId: Int, dateStart : DateTime, noOfDays : Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dataRepository.getWeatherDateRange(woeId, dateStart, noOfDays)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
    fun getFutureWeather(woeId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dataRepository.getCurrentWeather(woeId)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}