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

package com.ctemkar.weather.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ctemkar.weather.database.WeatherDatabase
import com.ctemkar.weather.database.asDomainModel
import com.ctemkar.weather.domain.mwLocation
import com.ctemkar.weather.network.WeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Repository for fetching Weather data  from the network and storing them on disk
 */
class WeatherRepository(private val database: WeatherDatabase) {

    val locationInfo: LiveData<List<mwLocation>> =
        Transformations.map(database.weatherDao.getLocationInfo("40.793896,-73.94071")) {
            it.asDomainModel()
        }


    suspend fun getLocationInfo(latlong: String) {
        withContext(Dispatchers.IO) {
            Timber.d("getLocationInfo is called")
            val locationInfo =
                WeatherNetwork.weatherData.getLocationInfo(latlong)

            //// val playlist = DevByteNetwork.devbytes.getPlaylist().await()
            //// database.videoDao.insertAll(playlist.asDatabaseModel())
        }
    }
    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     */
    /*
    suspend fun refreshWeather() {
        withContext(Dispatchers.IO) {
            Timber.d("refresh weather is called");
            //// val playlist = DevByteNetwork.devbytes.getPlaylist().await()
            //// database.videoDao.insertAll(playlist.asDatabaseModel())
        }
    }
    suspend fun getLocationInfo(latlong : String) {
        withContext(Dispatchers.IO) {
            Timber.d("getLocationInfo is called");
            val locationInfo =
                WeatherNetwork.weatherData.getLocationInfo(latlong).await()

            //// val playlist = DevByteNetwork.devbytes.getPlaylist().await()
            //// database.videoDao.insertAll(playlist.asDatabaseModel())
        }
    }

     */

}
