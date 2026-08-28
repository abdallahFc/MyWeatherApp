package com.example.myweather.presentation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myweather.presentation.model.WeatherContentState
import com.example.myweather.presentation.ui.PreviewCityNotFoundError
import com.example.myweather.presentation.ui.PreviewSurface
import com.example.myweather.presentation.ui.PreviewWeather

@Composable
fun WeatherContent(
    contentState: WeatherContentState,
    onRetryClicked: () -> Unit,
    onEditSearchClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = contentState,
        transitionSpec = {
            (fadeIn(tween(280)) + slideInVertically(tween(280)) { height -> height / 16 })
                .togetherWith(fadeOut(tween(160)))
        },
        contentKey = { state -> state.animationKey() },
        label = "weatherContent",
        modifier = modifier,
    ) { state ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (state) {
                WeatherContentState.Empty -> WeatherEmptyContent()

                is WeatherContentState.Loading ->
                    WeatherLoadingContent(refreshingCityName = state.refreshingCityName)

                is WeatherContentState.Success -> WeatherSuccessContent(weather = state.weather)

                is WeatherContentState.Error -> WeatherErrorContent(
                    error = state.error,
                    onRetryClicked = onRetryClicked,
                    onEditSearchClicked = onEditSearchClicked,
                )
            }
        }
    }
}

private fun WeatherContentState.animationKey(): Any = when (this) {
    WeatherContentState.Empty -> "empty"
    is WeatherContentState.Loading -> "loading"
    is WeatherContentState.Success -> "success:${weather.cityName}"
    is WeatherContentState.Error -> "error:${error.titleRes}"
}

@Preview
@Composable
private fun WeatherContentEmptyPreview() {
    PreviewSurface {
        WeatherContent(
            contentState = WeatherContentState.Empty,
            onRetryClicked = {},
            onEditSearchClicked = {},
        )
    }
}

@Preview
@Composable
private fun WeatherContentSuccessPreview() {
    PreviewSurface {
        WeatherContent(
            contentState = WeatherContentState.Success(PreviewWeather),
            onRetryClicked = {},
            onEditSearchClicked = {},
        )
    }
}

@Preview
@Composable
private fun WeatherContentErrorPreview() {
    PreviewSurface {
        WeatherContent(
            contentState = WeatherContentState.Error(PreviewCityNotFoundError),
            onRetryClicked = {},
            onEditSearchClicked = {},
        )
    }
}
