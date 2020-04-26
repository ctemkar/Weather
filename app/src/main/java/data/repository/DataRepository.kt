package data.repository

import network.ApiHelper
import network.LocationInfo

class DataRepository(private val apiHelper: ApiHelper) {

    suspend fun getWeather() = apiHelper.getWeather()
    suspend fun getLocationInfo(latlong : String) : LocationInfo? {
      val locations = apiHelper.getLocationInfo(latlong)
        if(locations.size > 0)
            return locations.get(0)
        else
            return null
    }
}