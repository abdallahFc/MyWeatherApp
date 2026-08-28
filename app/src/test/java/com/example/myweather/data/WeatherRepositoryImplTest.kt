package com.example.myweather.data

import com.example.myweather.data.local.LastCityLocalDataSource
import com.example.myweather.data.remote.WeatherRemoteDataSource
import com.example.myweather.data.remote.WeatherRequestException
import com.example.myweather.data.remote.dto.CurrentConditionDto
import com.example.myweather.data.remote.dto.NearestAreaDto
import com.example.myweather.data.remote.dto.TextValueDto
import com.example.myweather.data.remote.dto.WeatherResponseDto
import com.example.myweather.data.repository.WeatherRepositoryImpl
import com.example.myweather.domain.model.WeatherCondition
import com.example.myweather.domain.model.WeatherFailure
import com.example.myweather.domain.model.WeatherResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherRepositoryImplTest {

    @Test
    fun `a successful load is mapped to domain and becomes the stored city`() = runTest {
        val localDataSource = FakeLastCityLocalDataSource()
        val repository = WeatherRepositoryImpl(
            weatherRemoteDataSource = FakeWeatherRemoteDataSource(response = cairoResponse),
            lastCityLocalDataSource = localDataSource,
        )

        val result = repository.getCurrentWeather("cairo")

        assertTrue(result is WeatherResult.Success)
        val weather = (result as WeatherResult.Success).weather
        assertEquals("Cairo", weather.cityName)
        assertEquals(28, weather.temperatureCelsius)
        assertEquals(WeatherCondition.Clear, weather.conditionKind)
        assertEquals(22, weather.humidityPercent)
        assertEquals("Cairo", localDataSource.storedCity)
    }

    @Test
    fun `an unknown city is reported as CityNotFound and does not replace the stored city`() = runTest {
        val localDataSource = FakeLastCityLocalDataSource(storedCity = "Cairo")
        val repository = WeatherRepositoryImpl(
            weatherRemoteDataSource = FakeWeatherRemoteDataSource(
                failure = WeatherFailure.CityNotFound,
            ),
            lastCityLocalDataSource = localDataSource,
        )

        val result = repository.getCurrentWeather("InvalidCity123")

        assertEquals(WeatherResult.Failure(WeatherFailure.CityNotFound), result)
        assertEquals("Cairo", localDataSource.storedCity)
    }

    @Test
    fun `an unusable payload is reported as a server failure`() = runTest {
        val localDataSource = FakeLastCityLocalDataSource()
        val repository = WeatherRepositoryImpl(
            weatherRemoteDataSource = FakeWeatherRemoteDataSource(
                response = WeatherResponseDto(currentCondition = emptyList(), nearestArea = null),
            ),
            lastCityLocalDataSource = localDataSource,
        )

        val result = repository.getCurrentWeather("Cairo")

        assertEquals(WeatherResult.Failure(WeatherFailure.ServerFailure), result)
        assertNull(localDataSource.storedCity)
    }

    private val cairoResponse = WeatherResponseDto(
        currentCondition = listOf(
            CurrentConditionDto(
                temperatureCelsius = "28",
                weatherCode = "113",
                weatherDescription = listOf(TextValueDto("Sunny ")),
                feelsLikeCelsius = "30",
                humidityPercent = "22",
                windSpeedKmh = "11",
            ),
        ),
        nearestArea = listOf(NearestAreaDto(areaName = listOf(TextValueDto("Cairo")))),
    )
}

private class FakeWeatherRemoteDataSource(
    private val response: WeatherResponseDto? = null,
    private val failure: WeatherFailure? = null,
) : WeatherRemoteDataSource {
    override suspend fun getCurrentWeather(city: String): WeatherResponseDto {
        failure?.let { throw WeatherRequestException(it) }
        return requireNotNull(response)
    }
}

private class FakeLastCityLocalDataSource(
    var storedCity: String? = null,
) : LastCityLocalDataSource {
    override suspend fun getLastSearchedCity(): String? = storedCity
    override suspend fun saveLastSearchedCity(city: String) {
        storedCity = city
    }
}
