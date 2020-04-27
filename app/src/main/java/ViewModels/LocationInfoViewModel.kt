package ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.soywiz.klock.DateTime
import data.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import utils.Resource

class LocationInfoViewModel(private val dataRepository: DataRepository) : ViewModel() {

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