package com.example.myweather.presentation.model

sealed interface WeatherContentState {

    data object Empty : WeatherContentState

    data class Loading(val refreshingCityName: String? = null) : WeatherContentState

    data class Success(val weather: WeatherUiModel) : WeatherContentState

    data class Error(val error: WeatherErrorUiModel) : WeatherContentState
}
