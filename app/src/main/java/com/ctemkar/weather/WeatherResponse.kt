package com.ctemkar.weather

import com.google.gson.annotations.SerializedName

class WeatherResponse {
    // [{"title":"New York","location_type":"City","woeid":2459115,"latt_long":"40.71455,-74.007118"}]
    @SerializedName("title")
    var title: String? = null

    @SerializedName("location_type")
    var location_type: String? = null

    @SerializedName("woeid")
    var woeid: String? = null

    @SerializedName("latt_long")
    var lat_long: String? = null

}


