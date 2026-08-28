package com.example.myweather.presentation.model

import androidx.annotation.StringRes

data class WeatherUiModel(
    val cityName: String,
    val temperature: String,
    @param:StringRes val conditionRes: Int,
    val visual: WeatherVisual,
    val details: List<WeatherDetailUiModel>,
)

data class WeatherDetailUiModel(
    @param:StringRes val labelRes: Int,
    val value: String,
)
