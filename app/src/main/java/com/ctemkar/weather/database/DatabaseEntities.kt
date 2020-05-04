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

package com.ctemkar.weather.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ctemkar.weather.domain.mwLocation

@Entity
data class dbMwLocation constructor(
// Query - https://www.metaweather.com/api/location/search/?lattlong=40.793896,-73.94071
// result -  {"distance":10440,"title":"New York","location_type":"City","woeid":2459115,"latt_long":"40.71455,-74.007118"}
        @PrimaryKey
    val woeid: Int,
    val distance: Int,
    val title: String,
    val location_type: String,
    val latt_long: String
)

/**
 * Map DatabaseVideos to domain entities
 */
fun List<dbMwLocation>.asDomainModel(): List<mwLocation> {
    return map {
        mwLocation(
            distance = it.distance,
            title = it.title,
            location_type = it.location_type,
            woeid = it.woeid,
            latt_long = it.latt_long
        )
    }
}


/**
 * WeatherDay represents a days weather in the database for a particular woeid.
 */
/*
@Entity(primaryKeys = arrayOf("woeId", "applicable_date"))
data class DbWeatherDay constructor(
//        val woeid: Int,
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
*/
/**
 * Map WeatherDay to domain entities
 */
/*
fun List<DbWeatherDay>.asDomainModel(): List<DomWeatherDay> {
        return map {
                DomWeatherDay(
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
                        wind_speed = it.wind_speed
                )

        }
}
*/
