package model

data class WeatherInfo(
    val air_pressure: Double,
    val applicable_date: String,
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
)