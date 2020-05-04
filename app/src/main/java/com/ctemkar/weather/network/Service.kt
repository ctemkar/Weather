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

import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.coroutines.Deferred
import network.LocationInfo
import network.RetrofitBuilder
import network.RetrofitBuilder.okHttpClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import utils.Constant.BASE_URL

// Since we only have one service, this can all go in one file.
// If you add more services, split this to multiple files and make sure to share the retrofit
// object between services.

/**
 * A retrofit service to fetch a Weather for the day.
 */

var okHttpClient = OkHttpClient.Builder()
    .addNetworkInterceptor(StethoInterceptor())
    .build()

interface WeatherService {
/*    @GET("devbytes")
    fun getWeather(): Deferred<NetworkWeatherContainer>

 */
    @GET("/api/location/search")
//    suspend fun getLocationInfo(@Query("lattlong") latlong: String): Deferred<NetworkWeatherLocation>
    suspend fun getLocationInfo(@Query("lattlong") latlong: String): List<LocationInfo>

}

/**
 * Main entry point for network access. Call like `WeatherNetwork.devbytes.getPlaylist()`
 */
object WeatherNetwork {

/*    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
            .baseUrl("https://android-kotlin-fun-mars-server.appspot.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
*/
private val retrofit =
     Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build()


    val weatherData = retrofit.create(WeatherService::class.java)

}


