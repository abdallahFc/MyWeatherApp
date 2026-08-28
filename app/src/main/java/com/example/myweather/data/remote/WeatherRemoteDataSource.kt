package com.example.myweather.data.remote

import com.example.myweather.data.remote.dto.WeatherResponseDto
import com.example.myweather.domain.model.WeatherFailure

interface WeatherRemoteDataSource {

    suspend fun getCurrentWeather(city: String): WeatherResponseDto
}

class WeatherRequestException(val failure: WeatherFailure) : Exception()
