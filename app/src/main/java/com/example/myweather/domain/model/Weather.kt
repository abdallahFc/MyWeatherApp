package com.example.myweather.domain.model

data class Weather(
    val cityName: String,
    val temperatureCelsius: Int,
    val condition: String,
    val conditionKind: WeatherCondition,
    val feelsLikeCelsius: Int?,
    val humidityPercent: Int?,
    val windSpeedKmh: Int?,
)
