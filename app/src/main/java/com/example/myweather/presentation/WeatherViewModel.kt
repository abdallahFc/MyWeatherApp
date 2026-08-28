package com.example.myweather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myweather.MyWeatherApplication
import com.example.myweather.domain.model.WeatherResult
import com.example.myweather.domain.repository.WeatherRepository
import com.example.myweather.presentation.interaction.WeatherInteractionListener
import com.example.myweather.presentation.mapper.WeatherUiMapper
import com.example.myweather.presentation.model.ThemeMode
import com.example.myweather.presentation.model.WeatherContentState
import com.example.myweather.presentation.model.WeatherUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
) : ViewModel(), WeatherInteractionListener {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var lastSubmittedCity: String? = null

    init {
        restoreLastSearchedCity()
    }

    override fun onCityChanged(city: String) {
        _uiState.update { it.copy(cityQuery = city, showBlankCityError = false) }
    }

    override fun onClearCityClicked() {
        _uiState.update { it.copy(cityQuery = "", showBlankCityError = false) }
    }

    override fun onSearchClicked() {
        val city = _uiState.value.cityQuery.trim()
        if (city.isEmpty()) {
            _uiState.update { it.copy(showBlankCityError = true) }
            return
        }
        val isAlreadyRunning = searchJob?.isActive == true && lastSubmittedCity.equals(city, ignoreCase = true)
        if (isAlreadyRunning) return
        search(city)
    }

    override fun onRetryClicked() {
        val city = lastSubmittedCity
        if (city == null) onSearchClicked() else search(city)
    }

    override fun onThemeModeChanged(themeMode: ThemeMode) {
        _uiState.update { it.copy(themeMode = themeMode) }
    }

    private fun restoreLastSearchedCity() {
        viewModelScope.launch {
            val city = weatherRepository.getLastSearchedCity() ?: return@launch
            _uiState.update { it.copy(cityQuery = city) }
            search(city, isRestoringLastCity = true)
        }
    }

    private fun search(city: String, isRestoringLastCity: Boolean = false) {
        searchJob?.cancel()
        lastSubmittedCity = city
        val refreshingCityName = if (isRestoringLastCity) city else null
        _uiState.update {
            it.copy(
                showBlankCityError = false,
                contentState = WeatherContentState.Loading(refreshingCityName),
            )
        }
        searchJob = viewModelScope.launch {
            val contentState = when (val result = weatherRepository.getCurrentWeather(city)) {
                is WeatherResult.Success ->
                    WeatherContentState.Success(WeatherUiMapper.toUiModel(result.weather))

                is WeatherResult.Failure ->
                    WeatherContentState.Error(WeatherUiMapper.toErrorUiModel(result.failure))
            }
            _uiState.update { it.copy(contentState = contentState) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as MyWeatherApplication
                WeatherViewModel(application.appContainer.weatherRepository)
            }
        }
    }
}
