package com.example.myweather.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myweather.R
import com.example.myweather.presentation.interaction.WeatherInteractionListener
import com.example.myweather.presentation.model.ThemeMode
import com.example.myweather.presentation.model.WeatherContentState
import com.example.myweather.presentation.model.WeatherUiState
import com.example.myweather.presentation.ui.components.CitySearchField
import com.example.myweather.presentation.ui.components.ThemeToggle
import com.example.myweather.presentation.ui.components.WeatherContent
import com.example.myweather.ui.theme.MyWeatherTheme

private val ScreenPadding = 20.dp

@Composable
fun WeatherScreenContent(
    state: WeatherUiState,
    interactionListener: WeatherInteractionListener,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val searchFieldFocusRequester = remember { FocusRequester() }

    LaunchedEffect(state.contentState) {
        if (state.contentState is WeatherContentState.Loading) focusManager.clearFocus()
    }

    Scaffold(modifier = modifier) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = ScreenPadding),
        ) {
            WeatherHeader(
                themeMode = state.themeMode,
                onThemeModeChanged = interactionListener::onThemeModeChanged,
                modifier = Modifier.padding(top = 12.dp),
            )

            Spacer(Modifier.height(16.dp))

            CitySearchField(
                city = state.cityQuery,
                showBlankCityError = state.showBlankCityError,
                focusRequester = searchFieldFocusRequester,
                onCityChanged = interactionListener::onCityChanged,
                onClearCityClicked = interactionListener::onClearCityClicked,
                onSearchClicked = interactionListener::onSearchClicked,
            )

            Box(Modifier.weight(1f).fillMaxWidth()) {
                WeatherContent(
                    contentState = state.contentState,
                    onRetryClicked = interactionListener::onRetryClicked,
                    onEditSearchClicked = { searchFieldFocusRequester.requestFocus() },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .verticalScroll(rememberScrollState()),
                )
            }
        }
    }
}

@Composable
private fun WeatherHeader(
    themeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.app_tagline),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        ThemeToggle(themeMode = themeMode, onThemeModeChanged = onThemeModeChanged)
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherScreenSuccessPreview() {
    MyWeatherTheme(darkTheme = false) {
        WeatherScreenContent(
            state = WeatherUiState(
                cityQuery = "Cairo",
                contentState = WeatherContentState.Success(PreviewWeather),
            ),
            interactionListener = PreviewInteractionListener,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherScreenErrorPreview() {
    MyWeatherTheme(darkTheme = false) {
        WeatherScreenContent(
            state = WeatherUiState(
                cityQuery = "Xyzzy",
                contentState = WeatherContentState.Error(PreviewCityNotFoundError),
            ),
            interactionListener = PreviewInteractionListener,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherScreenEmptyDarkPreview() {
    MyWeatherTheme(darkTheme = true) {
        WeatherScreenContent(
            state = WeatherUiState(themeMode = ThemeMode.Dark),
            interactionListener = PreviewInteractionListener,
        )
    }
}
