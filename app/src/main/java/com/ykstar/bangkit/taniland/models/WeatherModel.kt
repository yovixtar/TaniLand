package com.ykstar.bangkit.taniland.models

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("weather") val weatherList: List<Weather>,
    @SerializedName("main") val main: Main,
    @SerializedName("dt") val date: Long
)

data class Weather(
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String
)

data class Main(
    @SerializedName("temp") val temp: Double
)
