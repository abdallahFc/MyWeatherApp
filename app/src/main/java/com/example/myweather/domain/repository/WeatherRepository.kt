package com.example.myweather.domain.repository

import com.example.myweather.domain.model.WeatherResult

interface WeatherRepository {

    suspend fun getCurrentWeather(city: String): WeatherResult

    suspend fun getLastSearchedCity(): String?
}
