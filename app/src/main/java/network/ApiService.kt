package network

import JsonWeather
import model.WeatherInfo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
   @GET("/api/location/2459115/2020/4/24/")
 //@GET("/api/location/search/2459115/")
    suspend fun getWeather(): List<WeatherInfo>

    @GET("/api/location/search")
    suspend fun getLocationInfo(@Query("lattlong") latlong : String) : List<LocationInfo>
    @GET("/api/location/{woeid}")
    suspend fun getCurrentWeather(@Path("woeid") woeId : Int) : JsonWeather


}
