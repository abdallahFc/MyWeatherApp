package com.example.myweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myweather.presentation.WeatherViewModel
import com.example.myweather.presentation.model.ThemeMode
import com.example.myweather.presentation.ui.WeatherScreenContent
import com.example.myweather.ui.theme.MyWeatherTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: WeatherViewModel = viewModel(factory = WeatherViewModel.Factory)
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            MyWeatherTheme(darkTheme = state.themeMode.isDarkTheme()) {
                WeatherScreenContent(state = state, interactionListener = viewModel)
            }
        }
    }
}

@Composable
private fun ThemeMode.isDarkTheme(): Boolean = when (this) {
    ThemeMode.System -> isSystemInDarkTheme()
    ThemeMode.Light -> false
    ThemeMode.Dark -> true
}
