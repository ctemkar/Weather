
import com.google.gson.annotations.SerializedName
import model.WeatherInfo


class  JsonWeather{
        @SerializedName("consolidated_weather")
        var weatherItems : List<WeatherInfo>? = null
    }

