package network


class ApiHelper(private val apiService: ApiService) {

    suspend fun getWeather() = apiService.getWeather()
    suspend fun getLocationInfo(latlong : String) = apiService.getLocationInfo(latlong)
    suspend fun getCurrentWeather(woeId : Int) = apiService.getCurrentWeather(woeId)
    suspend fun getWeatherOnDate(woeId: Int, year: Int, month : Int, day : Int ) = apiService.getWeatherOnDate(woeId, year, month, day)
}