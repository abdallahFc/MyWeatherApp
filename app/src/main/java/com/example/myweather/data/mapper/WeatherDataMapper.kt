package com.example.myweather.data.mapper

import com.example.myweather.data.remote.dto.TextValueDto
import com.example.myweather.data.remote.dto.WeatherResponseDto
import com.example.myweather.domain.model.Weather
import com.example.myweather.domain.model.WeatherCondition

object WeatherDataMapper {

    fun toDomain(dto: WeatherResponseDto, requestedCity: String): Weather? {
        val current = dto.currentCondition?.firstOrNull() ?: return null
        val temperature = current.temperatureCelsius.toTrimmedInt() ?: return null
        val condition = current.weatherDescription.firstValue() ?: return null

        return Weather(
            cityName = dto.nearestArea?.firstOrNull()?.areaName.firstValue() ?: requestedCity,
            temperatureCelsius = temperature,
            condition = condition,
            conditionKind = toCondition(current.weatherCode.toTrimmedInt()),
            feelsLikeCelsius = current.feelsLikeCelsius.toTrimmedInt(),
            humidityPercent = current.humidityPercent.toTrimmedInt(),
            windSpeedKmh = current.windSpeedKmh.toTrimmedInt(),
        )
    }

    private fun String?.toTrimmedInt(): Int? = this?.trim()?.toIntOrNull()

    private fun List<TextValueDto>?.firstValue(): String? =
        this?.firstOrNull()?.value?.trim()?.takeIf { it.isNotEmpty() }

    private fun toCondition(weatherCode: Int?): WeatherCondition {
        if (weatherCode == null) return WeatherCondition.Unknown
        return when (weatherCode) {
            CLEAR -> WeatherCondition.Clear
            in THUNDERSTORM_CODES -> WeatherCondition.Thunderstorm
            in SNOW_CODES -> WeatherCondition.Snow
            in RAIN_CODES -> WeatherCondition.Rain
            in CLOUDY_CODES -> WeatherCondition.Cloudy
            else -> WeatherCondition.Unknown
        }
    }

    private const val CLEAR = 113
    private val CLOUDY_CODES = setOf(116, 119, 122, 143, 248, 260)
    private val THUNDERSTORM_CODES = setOf(200, 386, 389, 392, 395)
    private val RAIN_CODES = setOf(176, 263, 266, 293, 296, 299, 302, 305, 308, 353, 356, 359)
    private val SNOW_CODES = setOf(
        179, 182, 185, 227, 230, 281, 284, 311, 314, 317, 320, 323, 326, 329, 332, 335,
        338, 350, 362, 365, 368, 371, 374, 377,
    )
}
