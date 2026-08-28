package com.example.myweather.presentation.model

import androidx.annotation.DrawableRes
import com.example.myweather.R

enum class WeatherVisual(@param:DrawableRes val iconRes: Int) {
    Sunny(R.drawable.ic_weather_sunny),
    Cloudy(R.drawable.ic_weather_cloudy),
    Rain(R.drawable.ic_weather_rainy),
    Snow(R.drawable.ic_weather_snowy),
    Storm(R.drawable.ic_weather_storm),
    Unknown(R.drawable.ic_weather_partly_cloudy),
}
