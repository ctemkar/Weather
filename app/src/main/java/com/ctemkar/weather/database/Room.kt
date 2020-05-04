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

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import model.WeatherInfo


@Dao
interface WeatherDao {
    @Query("select * from dbMwLocation where latt_long = :latlong")
    fun getLocationInfo(latlong : String) : LiveData<List<dbMwLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( locations: List<dbMwLocation>)

    @Query("select * from dbCurrentWeather where woeid = :woeId")
    fun getCurrentWeather(woeId : Int) : List<dbCurrentWeather>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentWeather( weather : List<dbCurrentWeather>)

    /*
    @Query("select * from databaseweather")
    fun getWeather(): LiveData<List<DatabaseWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( videos: List<DatabaseWeather>)

     */
}



@Database(entities = [dbCurrentWeather::class, dbMwLocation::class], version = 1)
abstract class WeatherDatabase: RoomDatabase() {
    abstract val weatherDao: WeatherDao
}



private lateinit var INSTANCE: WeatherDatabase

fun getDatabase(context: Context?): WeatherDatabase {
    synchronized(WeatherDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            if (context != null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather").build()
            }
        }
    }
    return INSTANCE
}
