package com.example.myweather.presentation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myweather.R
import com.example.myweather.presentation.model.WeatherDetailUiModel
import com.example.myweather.presentation.model.WeatherUiModel
import com.example.myweather.presentation.ui.PreviewSurface
import com.example.myweather.presentation.ui.PreviewWeather
import com.example.myweather.presentation.ui.PreviewWeatherWithoutDetails

@Composable
fun WeatherSuccessContent(
    weather: WeatherUiModel,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = weather.cityName, style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(14.dp))
        AnimatedContent(
            targetState = weather.visual,
            transitionSpec = { fadeIn(tween(320)) togetherWith fadeOut(tween(160)) },
            label = "weatherVisual",
        ) { visual ->
            Icon(
                painter = painterResource(visual.iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(76.dp),
            )
        }

        Spacer(Modifier.height(8.dp))
        Temperature(temperature = weather.temperature)

        Text(
            text = stringResource(weather.conditionRes),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (weather.details.isNotEmpty()) {
            Spacer(Modifier.height(28.dp))
            WeatherDetailStrip(details = weather.details)
        }
    }
}

@Composable
private fun Temperature(temperature: String, modifier: Modifier = Modifier) {
    val readOutLoud = stringResource(R.string.temperature_content_description, temperature)
    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = readOutLoud },
        verticalAlignment = Alignment.Top,
    ) {
        Text(text = temperature, style = MaterialTheme.typography.displayLarge)
        Text(
            text = stringResource(R.string.degree_symbol),
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Light),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun WeatherDetailStrip(
    details: List<WeatherDetailUiModel>,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Row(Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
            details.forEachIndexed { index, detail ->
                if (index > 0) VerticalDivider(Modifier.height(40.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(detail.labelRes),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(text = detail.value, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Preview
@Composable
private fun WeatherSuccessContentPreview() {
    PreviewSurface { WeatherSuccessContent(weather = PreviewWeather) }
}

@Preview
@Composable
private fun WeatherSuccessContentDarkPreview() {
    PreviewSurface(darkTheme = true) { WeatherSuccessContent(weather = PreviewWeather) }
}

@Preview
@Composable
private fun WeatherSuccessContentWithoutDetailsPreview() {
    PreviewSurface { WeatherSuccessContent(weather = PreviewWeatherWithoutDetails) }
}
