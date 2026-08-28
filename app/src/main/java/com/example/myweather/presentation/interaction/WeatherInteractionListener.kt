package com.example.myweather.presentation.interaction

import com.example.myweather.presentation.model.ThemeMode

interface WeatherInteractionListener {

    fun onCityChanged(city: String)

    fun onClearCityClicked()

    fun onSearchClicked()

    fun onRetryClicked()

    fun onThemeModeChanged(themeMode: ThemeMode)
}
