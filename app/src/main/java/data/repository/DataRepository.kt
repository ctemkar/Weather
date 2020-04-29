package data.repository

import android.util.Log
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.days
import com.soywiz.klock.parseUtc
import model.WeatherInfo
import network.ApiHelper
import network.LocationInfo
import kotlin.math.roundToInt

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
        val selectedItem = weatherItems?.get(weatherItems.size-1)
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
            selectedItem?.the_tempF = celciusToFahreniet((averageTemp))

        if(averageMinTemp != null)
            selectedItem?.min_tempF = celciusToFahreniet(averageMinTemp)
        if(averageMaxTemp != null)
            selectedItem?.max_tempF = celciusToFahreniet(averageMaxTemp)
        Log.d(TAG, "AVG - Current:" + selectedItem?.the_temp + ", Min:" + selectedItem?.min_temp + ", Max: " + selectedItem?.max_temp)
        Log.d(TAG, "CurrentF:" + selectedItem?.the_tempF + ", MinF:" + selectedItem?.min_tempF + ", MaxF: " + selectedItem?.max_tempF)


        if (weatherItems != null) {
                return selectedItem
        }
        else
            return null

    }

    private fun celciusToFahreniet(averageMinTemp: Double): Int {
        return (averageMinTemp * 9/5 + 32 ).roundToInt()
    }

    suspend fun getWeatherDateRange(woeId: Int, dateStart : DateTime, noOfDays : Int) : List<WeatherInfo> {
        val weatherInfoList = ArrayList<WeatherInfo>()
//        val stackWeatherInfoList = Stack<WeatherInfo>()
        Log.d(TAG, "Date: " + dateStart + 1.days)
        var date = dateStart
        val today = DateTime.now()
        val tomorrow = DateTime.now() + 1.days
        val dateFormat = DateFormat("yyyy-MM-dd") // Construct a new DateFormat from a String
        val dateDisplayFormatCurrentYear = DateFormat("EEE MMM, dd")
        val dateDispalyFromatOtherYears = DateFormat("MM dd, YYYY")

        for(i in 0 until noOfDays) {
           // Log.d(TAG, date.yearInt.toString() + "/" + date.month1.toString() + "/" + date.dayOfMonth.toString())
            val weatherInfo = getWeatherOnDate(woeId, date.yearInt, date.month1, date.dayOfMonth)
            if (weatherInfo.size> 0)   {
                val selectWeatherInfo = weatherInfo.get(0)
                val applicableDate = dateFormat.parseUtc(selectWeatherInfo.applicable_date)
                selectWeatherInfo.min_tempF = celciusToFahreniet(selectWeatherInfo.min_temp)
                selectWeatherInfo.max_tempF = celciusToFahreniet(selectWeatherInfo.max_temp)
                if(applicableDate.year == tomorrow.year && applicableDate.month == tomorrow.month
                    && applicableDate.dayOfMonth == tomorrow.dayOfMonth)
                    selectWeatherInfo.day = "Tomorrow"
                else
                    if(applicableDate.year == today.year)
                        selectWeatherInfo.day = applicableDate.format(dateDisplayFormatCurrentYear)
                    else
                        selectWeatherInfo.day = applicableDate.format((dateDispalyFromatOtherYears))
                weatherInfoList.add(selectWeatherInfo)

            } else
                break

            date += 1.days
        }

        weatherInfoList.reverse()
        return weatherInfoList
    }
    suspend fun getWeatherOnDate(woeId : Int, year : Int, month : Int, day : Int) =
        apiHelper.getWeatherOnDate(woeId, year, month, day)

}