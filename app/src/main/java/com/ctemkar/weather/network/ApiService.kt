package com.ctemkar.weather.network

import JsonWeather
import com.ctemkar.weather.model.WeatherInfo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("/api/location/2459115/2020/4/24/")
    //@GET("/api/location/search/2459115/")
    suspend fun getWeather(): List<WeatherInfo>

    @GET("/api/location/search")
    suspend fun getLocationInfo(@Query("lattlong") latlong: String): List<LocationInfo>

    @GET("/api/location/{woeid}")
    suspend fun getCurrentWeather(@Path("woeid") woeId: Int): JsonWeather

//    @GET("/api/location/{woeid}/{date}/")
    @GET("/api/location/{woeid}/{yyyy}/{mm}/{dd}")
    suspend fun getWeatherOnDate(@Path("woeid") woid: Int, @Path("yyyy" ) year : Int,
        @Path("mm") month : Int, @Path("dd") day : Int) : List<WeatherInfo>

}
