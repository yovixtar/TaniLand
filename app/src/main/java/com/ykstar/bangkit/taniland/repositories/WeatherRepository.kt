package com.ykstar.bangkit.taniland.repositories

import com.ykstar.bangkit.taniland.models.WeatherResponse
import com.ykstar.bangkit.taniland.network.WeatherAPIConfig

class WeatherRepository {
    private val weatherService = WeatherAPIConfig.createWeatherService()

    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse {
        return weatherService.getWeather(lat, lon)
    }
}