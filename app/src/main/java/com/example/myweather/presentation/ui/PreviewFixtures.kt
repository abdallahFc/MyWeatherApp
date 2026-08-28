package com.example.myweather.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myweather.domain.model.Weather
import com.example.myweather.domain.model.WeatherCondition
import com.example.myweather.domain.model.WeatherFailure
import com.example.myweather.presentation.interaction.WeatherInteractionListener
import com.example.myweather.presentation.mapper.WeatherUiMapper
import com.example.myweather.presentation.model.ThemeMode
import com.example.myweather.presentation.model.WeatherErrorUiModel
import com.example.myweather.presentation.model.WeatherUiModel
import com.example.myweather.ui.theme.MyWeatherTheme

internal val PreviewWeather: WeatherUiModel = WeatherUiMapper.toUiModel(
    Weather(
        cityName = "Cairo",
        temperatureCelsius = 28,
        condition = "Sunny",
        conditionKind = WeatherCondition.Clear,
        feelsLikeCelsius = 30,
        humidityPercent = 22,
        windSpeedKmh = 11,
    ),
)

internal val PreviewWeatherWithoutDetails: WeatherUiModel = PreviewWeather.copy(details = emptyList())

internal val PreviewCityNotFoundError: WeatherErrorUiModel =
    WeatherUiMapper.toErrorUiModel(WeatherFailure.CityNotFound)

internal val PreviewOfflineError: WeatherErrorUiModel =
    WeatherUiMapper.toErrorUiModel(WeatherFailure.NoInternet)

internal object PreviewInteractionListener : WeatherInteractionListener {
    override fun onCityChanged(city: String) = Unit
    override fun onClearCityClicked() = Unit
    override fun onSearchClicked() = Unit
    override fun onRetryClicked() = Unit
    override fun onThemeModeChanged(themeMode: ThemeMode) = Unit
}

@Composable
internal fun PreviewSurface(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MyWeatherTheme(darkTheme = darkTheme) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(Modifier.padding(20.dp)) { content() }
        }
    }
}
