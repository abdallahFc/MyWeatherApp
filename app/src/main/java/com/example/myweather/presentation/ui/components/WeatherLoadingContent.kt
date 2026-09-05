package com.example.myweather.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myweather.R
import com.example.myweather.presentation.ui.PreviewSurface

@Composable
fun WeatherLoadingContent(
    refreshingCityName: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CircularProgressIndicator(modifier = Modifier.size(40.dp), strokeWidth = 4.dp)
        Text(
            text = when (refreshingCityName) {
                null -> stringResource(R.string.weather_loading_message)
                else -> stringResource(R.string.weather_refreshing_message, refreshingCityName)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
private fun WeatherLoadingContentPreview() {
    PreviewSurface { WeatherLoadingContent(refreshingCityName = null) }
}

@Preview
@Composable
private fun WeatherLoadingContentRefreshingPreview() {
    PreviewSurface { WeatherLoadingContent(refreshingCityName = "cairo") }
}
