package com.example.myweather.presentation.mapper

import com.example.myweather.R
import com.example.myweather.domain.model.Weather
import com.example.myweather.domain.model.WeatherCondition
import com.example.myweather.domain.model.WeatherFailure
import com.example.myweather.presentation.model.WeatherDetailUiModel
import com.example.myweather.presentation.model.WeatherErrorAction
import com.example.myweather.presentation.model.WeatherErrorUiModel
import com.example.myweather.presentation.model.WeatherUiModel
import com.example.myweather.presentation.model.WeatherVisual

object WeatherUiMapper {

    fun toUiModel(weather: Weather): WeatherUiModel = WeatherUiModel(
        cityName = weather.cityName,
        temperature = weather.temperatureCelsius.toString(),
        conditionRes = toConditionRes(weather.conditionKind),
        visual = toVisual(weather.conditionKind),
        details = toDetails(weather),
    )

    fun toErrorUiModel(failure: WeatherFailure): WeatherErrorUiModel = when (failure) {
        WeatherFailure.NoInternet -> WeatherErrorUiModel(
            iconRes = R.drawable.ic_wifi_off,
            titleRes = R.string.error_offline_title,
            messageRes = R.string.error_offline_message,
            actionLabelRes = R.string.action_try_again,
            action = WeatherErrorAction.Retry,
        )

        WeatherFailure.CityNotFound -> WeatherErrorUiModel(
            iconRes = R.drawable.ic_search_off,
            titleRes = R.string.error_city_not_found_title,
            messageRes = R.string.error_city_not_found_message,
            actionLabelRes = R.string.action_edit_search,
            action = WeatherErrorAction.EditSearch,
        )

        WeatherFailure.ServerFailure -> WeatherErrorUiModel(
            iconRes = R.drawable.ic_cloud_off,
            titleRes = R.string.error_server_title,
            messageRes = R.string.error_server_message,
            actionLabelRes = R.string.action_try_again,
            action = WeatherErrorAction.Retry,
        )

        WeatherFailure.Unknown -> WeatherErrorUiModel(
            iconRes = R.drawable.ic_cloud_off,
            titleRes = R.string.error_unknown_title,
            messageRes = R.string.error_unknown_message,
            actionLabelRes = R.string.action_try_again,
            action = WeatherErrorAction.Retry,
        )
    }

    private fun toVisual(condition: WeatherCondition): WeatherVisual = when (condition) {
        WeatherCondition.Clear -> WeatherVisual.Sunny
        WeatherCondition.Cloudy -> WeatherVisual.Cloudy
        WeatherCondition.Rain -> WeatherVisual.Rain
        WeatherCondition.Snow -> WeatherVisual.Snow
        WeatherCondition.Thunderstorm -> WeatherVisual.Storm
        WeatherCondition.Unknown -> WeatherVisual.Unknown
    }

    private fun toConditionRes(condition: WeatherCondition): Int = when (condition) {
        WeatherCondition.Clear -> R.string.weather_condition_clear
        WeatherCondition.Cloudy -> R.string.weather_condition_cloudy
        WeatherCondition.Rain -> R.string.weather_condition_rain
        WeatherCondition.Snow -> R.string.weather_condition_snow
        WeatherCondition.Thunderstorm -> R.string.weather_condition_thunderstorm
        WeatherCondition.Unknown -> R.string.weather_condition_unknown
    }

    private fun toDetails(weather: Weather): List<WeatherDetailUiModel> = buildList {
        weather.feelsLikeCelsius?.let {
            add(WeatherDetailUiModel(R.string.detail_feels_like, "$it°"))
        }
        weather.humidityPercent?.let {
            add(WeatherDetailUiModel(R.string.detail_humidity, "$it%"))
        }
        weather.windSpeedKmh?.let {
            add(WeatherDetailUiModel(R.string.detail_wind, "$it km/h"))
        }
    }
}
