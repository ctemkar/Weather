package network

import com.google.gson.annotations.SerializedName
import model.WeatherInfo


data class TodayWeatherInfo(
    // {"distance":10440,"title":"New York","location_type":"City","woeid":2459115,"latt_long":"40.71455,-74.007118"}
    @SerializedName("consolidated_weather")
    var weatherItems : List<WeatherInfo>
)