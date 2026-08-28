package com.example.myweather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    @SerializedName("current_condition") val currentCondition: List<CurrentConditionDto>?,
    @SerializedName("nearest_area") val nearestArea: List<NearestAreaDto>?,
)

data class CurrentConditionDto(
    @SerializedName("temp_C") val temperatureCelsius: String?,
    @SerializedName("weatherCode") val weatherCode: String?,
    @SerializedName("weatherDesc") val weatherDescription: List<TextValueDto>?,
    @SerializedName("FeelsLikeC") val feelsLikeCelsius: String?,
    @SerializedName("humidity") val humidityPercent: String?,
    @SerializedName("windspeedKmph") val windSpeedKmh: String?,
)

data class NearestAreaDto(
    @SerializedName("areaName") val areaName: List<TextValueDto>?,
)

data class TextValueDto(
    @SerializedName("value") val value: String?,
)
