package com.example.myweather.presentation.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.myweather.R
import com.example.myweather.presentation.ui.PreviewSurface

@Composable
fun WeatherEmptyContent(modifier: Modifier = Modifier) {
    WeatherMessage(
        iconRes = R.drawable.ic_weather_partly_cloudy,
        iconTint = MaterialTheme.colorScheme.primary,
        plateColor = MaterialTheme.colorScheme.surfaceContainer,
        title = stringResource(R.string.weather_empty_title),
        message = stringResource(R.string.weather_empty_message),
        modifier = modifier,
    )
}

@Preview
@Composable
private fun WeatherEmptyContentPreview() {
    PreviewSurface { WeatherEmptyContent() }
}

@Preview
@Composable
private fun WeatherEmptyContentDarkPreview() {
    PreviewSurface(darkTheme = true) { WeatherEmptyContent() }
}
