/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ctemkar.weather.network

import com.ctemkar.weather.database.dbMwLocation
import com.ctemkar.weather.domain.WeatherDay
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 *
 * @see "domain" package for
 */

/**
 * VideoHolder holds a list of Videos.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "videos": []
 * }
 */

@JsonClass(generateAdapter = true)
data class NetworkWeatherLocationContainer(val videos: List<NetworkWeatherLocation>)

/**
 * Videos represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkWeatherLocation(
    val distance: Int,
    val title: String,
    val location_type: String,
    val woeid: Int,
    val latt_long: String)


fun NetworkWeatherLocationContainer.asDatabaseModel(): List<dbMwLocation> {
    return videos.map {
        dbMwLocation(
            distance = it.distance,
            title = it.title,
            location_type = it.location_type,
            woeid = it.woeid,
            latt_long = it.latt_long)
    }
}

@JsonClass(generateAdapter = true)
data class NetworkWeatherContainer(val weather: List<NetworkWeather>)

/**
 * Videos represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkWeather(
    val id: Long,
    val air_pressure: Double,
    val applicable_date: String,
    var day : String,
    val created: String,
    val humidity: Int,
    var max_temp: Double,
    var min_temp: Double,
    var max_tempF: Int,
    var min_tempF: Int,
    val predictability: Int,
    val the_temp: Double,
    var the_tempF: Int,
    val visibility: Double,
    val weather_state_abbr: String,
    val weather_state_name: String,
    val wind_direction: Double,
    val wind_direction_compass: String,
    val wind_speed: Double)

/**
 * Convert Network results to database objects
 */
fun NetworkWeatherContainer.asDomainModel(): List<WeatherDay> {
    return weather.map {
        WeatherDay(
            air_pressure = it.air_pressure,
            applicable_date = it.applicable_date,
            day = it.day,
            created = it.created,
            humidity = it.humidity,
            id = it.id,
            max_temp = it.max_temp,
            min_temp = it.min_temp,
            min_tempF = it.min_tempF,
            max_tempF = it.max_tempF,
            predictability = it.predictability,
            the_temp = it.the_temp,
            the_tempF = it.the_tempF,
            visibility = it.visibility,
            weather_state_abbr = it.weather_state_abbr,
            weather_state_name = it.weather_state_name,
            wind_direction = it.wind_direction,
            wind_direction_compass = it.wind_direction_compass,
            wind_speed = it.wind_speed)
    }
}


/**
 * Convert Network results to database objects
 */
/*
fun NetworkWeatherContainer.asDatabaseModel(): List<DatabaseWeather> {
    return weather.map {
        DatabaseWeather(
            air_pressure = it.air_pressure,
            applicable_date = it.applicable_date,
            day = it.day,
            created = it.created,
            humidity = it.humidity,
            id = it.id,
            max_temp = it.max_temp,
            min_temp = it.min_temp,
            min_tempF = it.min_tempF,
            max_tempF = it.max_tempF,
            predictability = it.predictability,
            the_temp = it.the_temp,
            the_tempF = it.the_tempF,
            visibility = it.visibility,
            weather_state_abbr = it.weather_state_abbr,
            weather_state_name = it.weather_state_name,
            wind_direction = it.wind_direction,
            wind_direction_compass = it.wind_direction_compass,
            wind_speed = it.wind_speed)
    }
}

 */

