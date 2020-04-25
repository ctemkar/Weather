package network

import model.WeatherByDates
import retrofit2.http.GET


interface ApiService {
    @GET("/api/location/2459115/2020/4/24/")
    suspend fun getWeather(): List<WeatherByDates>

}
