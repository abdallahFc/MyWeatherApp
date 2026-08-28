package com.example.myweather.domain.model

sealed interface WeatherResult {
    data class Success(val weather: Weather) : WeatherResult
    data class Failure(val failure: WeatherFailure) : WeatherResult
}
