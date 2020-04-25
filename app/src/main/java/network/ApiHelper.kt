package network


class ApiHelper(private val apiService: ApiService) {

    suspend fun getWeather() = apiService.getWeather()
}