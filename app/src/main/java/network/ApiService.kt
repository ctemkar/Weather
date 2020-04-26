package network

import model.WeatherByDates
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("/api/location/2459115/2020/4/24/")
    suspend fun getWeather(): List<WeatherByDates>

    @GET("/api/location/search")
    suspend fun getLocationInfo(@Query("lattlong") latlong : String) : List<LocationInfo>


}
