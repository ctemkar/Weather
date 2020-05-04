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

package com.ctemkar.weather.domain

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 *
  */

data class mwLocation(
    val distance: Int,
    val title: String,
    val location_type: String,
    val woeid: Int,
    val latt_long: String
)

/**
 * Videos represent a devbyte that can be played.
 */
data class WeatherDay(
    val air_pressure: Double,
    val applicable_date: String,
    var day : String,
    val created: String,
    val humidity: Int,
    val id: Long,
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
    val wind_speed: Double


) {


    // val shortDescription: String
    //     get() = description.smartTruncate(200)
}