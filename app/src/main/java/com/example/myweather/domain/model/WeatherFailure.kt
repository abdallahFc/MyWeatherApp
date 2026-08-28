package com.example.myweather.domain.model

sealed interface WeatherFailure {
    data object NoInternet : WeatherFailure
    data object CityNotFound : WeatherFailure
    data object ServerFailure : WeatherFailure
    data object Unknown : WeatherFailure
}
