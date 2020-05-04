package com.ctemkar.weather.network


data class LocationInfo(
    // {"distance":10440,"title":"New York","location_type":"City","woeid":2459115,"latt_long":"40.71455,-74.007118"}
    val distance: Int,
    val title: String,
    val location_type: String,
    val woeid: Int,
    val latt_long: String
)