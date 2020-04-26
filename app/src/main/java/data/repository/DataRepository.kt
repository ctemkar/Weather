package data.repository

import android.util.Log
import model.WeatherInfo
import network.ApiHelper
import network.LocationInfo

class DataRepository(private val apiHelper: ApiHelper) {
    private val TAG = "DataRepository"
    suspend fun getWeather() = apiHelper.getWeather()
    suspend fun getLocationInfo(latlong : String) : LocationInfo? {
      val locations = apiHelper.getLocationInfo(latlong)
        if(locations.size > 0)
            // Assume for now that the first record is the nearest weather record
            return locations.get(0)
        else
            return null
    }
    suspend fun getCurrentWeather(woeId : Int) : WeatherInfo? {
        val weatherItemsList = apiHelper.getCurrentWeather(woeId)
        val weatherItems = weatherItemsList.weatherItems
        var selectedItem = weatherItems?.get(weatherItems.size-1)
       if(selectedItem != null) {
            selectedItem.the_tempF = ((selectedItem.the_temp * 9/ 5) + 32).toInt()
            selectedItem.min_tempF = ((selectedItem.min_temp * 9/ 5) + 32).toInt()
            selectedItem.max_tempF = ((selectedItem.max_temp * 9/ 5) + 32).toInt()

        }
        Log.d(TAG, "Current:" + selectedItem?.the_temp + ", Min:" + selectedItem?.min_temp + ", Max: " + selectedItem?.max_temp)
        Log.d(TAG, "CurrentF:" + selectedItem?.the_tempF + ", MinF:" + selectedItem?.min_tempF + ", MaxF: " + selectedItem?.max_tempF)

        val averageTemp = weatherItems?.map { it->it.the_temp }?.average()

        val averageMinTemp = weatherItems?.map { it->it.min_temp }?.average()
        val averageMaxTemp = weatherItems?.map { it->it.max_temp }?.average()
        if(averageTemp != null)
            selectedItem?.the_tempF = (averageTemp * 9/5 + 32).toInt()

        if(averageMinTemp != null)
            selectedItem?.min_tempF = (averageMinTemp * 9/5 + 32).toInt()
        if(averageMaxTemp != null)
            selectedItem?.max_tempF = (averageMaxTemp * 9/5 + 32).toInt()


        if (weatherItems != null) {
            for (weatherItem in weatherItems) {

            }

                return selectedItem
        }
        else
            return null

    }
}