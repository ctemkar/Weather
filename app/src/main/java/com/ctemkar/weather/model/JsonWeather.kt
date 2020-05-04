
import com.google.gson.annotations.SerializedName
import com.ctemkar.weather.model.WeatherInfo


class  JsonWeather{
        @SerializedName("consolidated_weather")
        var weatherItems : List<WeatherInfo>? = null
    }

