package com.example.myweather.presentation.model

data class WeatherUiState(
    val cityQuery: String = "",
    val showBlankCityError: Boolean = false,
    val contentState: WeatherContentState = WeatherContentState.Empty,
    val themeMode: ThemeMode = ThemeMode.System,
)
