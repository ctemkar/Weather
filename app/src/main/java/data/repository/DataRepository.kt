package data.repository

import network.ApiHelper

class DataRepository(private val apiHelper: ApiHelper) {

    suspend fun getWeather() = apiHelper.getWeather()
}