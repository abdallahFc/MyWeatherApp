package com.example.myweather.presentation

import com.example.myweather.domain.model.Weather
import com.example.myweather.domain.model.WeatherCondition
import com.example.myweather.domain.model.WeatherFailure
import com.example.myweather.domain.model.WeatherResult
import com.example.myweather.domain.repository.WeatherRepository
import com.example.myweather.presentation.model.WeatherContentState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search shows loading and then the weather`() = runTest(testDispatcher) {
        val viewModel = WeatherViewModel(FakeWeatherRepository(weatherFor = mapOf("Cairo" to cairo)))

        viewModel.onCityChanged("  Cairo  ")
        viewModel.onSearchClicked()

        assertEquals(WeatherContentState.Loading(), viewModel.uiState.value.contentState)

        advanceUntilIdle()

        val contentState = viewModel.uiState.value.contentState
        assertTrue(contentState is WeatherContentState.Success)
        assertEquals("Cairo", (contentState as WeatherContentState.Success).weather.cityName)
        assertEquals("28", contentState.weather.temperature)
    }

    @Test
    fun `a slow first search never overwrites a faster second one`() = runTest(testDispatcher) {
        val repository = FakeWeatherRepository(
            weatherFor = mapOf("Cairo" to cairo, "London" to london),
            delayMillisFor = mapOf("Cairo" to 1_000L, "London" to 10L),
        )
        val viewModel = WeatherViewModel(repository)

        viewModel.onCityChanged("Cairo")
        viewModel.onSearchClicked()
        viewModel.onCityChanged("London")
        viewModel.onSearchClicked()

        advanceUntilIdle()

        val contentState = viewModel.uiState.value.contentState
        assertTrue(contentState is WeatherContentState.Success)
        assertEquals("London", (contentState as WeatherContentState.Success).weather.cityName)
    }

    @Test
    fun `a blank city is reported instead of requested`() = runTest(testDispatcher) {
        val repository = FakeWeatherRepository(weatherFor = mapOf("Cairo" to cairo))
        val viewModel = WeatherViewModel(repository)

        viewModel.onCityChanged("   ")
        viewModel.onSearchClicked()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.showBlankCityError)
        assertEquals(WeatherContentState.Empty, viewModel.uiState.value.contentState)
        assertEquals(0, repository.requestedCities.size)
    }

    @Test
    fun `a stored city is reloaded on start`() = runTest(testDispatcher) {
        val repository = FakeWeatherRepository(
            weatherFor = mapOf("Cairo" to cairo),
            storedCity = "Cairo",
        )
        val viewModel = WeatherViewModel(repository)
        advanceUntilIdle()

        assertEquals("Cairo", viewModel.uiState.value.cityQuery)
        assertTrue(viewModel.uiState.value.contentState is WeatherContentState.Success)
    }

    private val cairo = weather(city = "Cairo", temperature = 28)
    private val london = weather(city = "London", temperature = 14)

    private fun weather(city: String, temperature: Int) = Weather(
        cityName = city,
        temperatureCelsius = temperature,
        condition = "Sunny",
        conditionKind = WeatherCondition.Clear,
        feelsLikeCelsius = null,
        humidityPercent = null,
        windSpeedKmh = null,
    )
}

private class FakeWeatherRepository(
    private val weatherFor: Map<String, Weather> = emptyMap(),
    private val delayMillisFor: Map<String, Long> = emptyMap(),
    private val storedCity: String? = null,
) : WeatherRepository {

    val requestedCities = mutableListOf<String>()

    override suspend fun getCurrentWeather(city: String): WeatherResult {
        requestedCities += city
        delayMillisFor[city]?.let { delay(it) }
        val weather = weatherFor[city]
            ?: return WeatherResult.Failure(WeatherFailure.CityNotFound)
        return WeatherResult.Success(weather)
    }

    override suspend fun getLastSearchedCity(): String? = storedCity
}
