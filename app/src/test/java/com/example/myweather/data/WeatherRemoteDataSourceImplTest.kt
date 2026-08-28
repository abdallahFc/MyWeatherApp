package com.example.myweather.data

import com.example.myweather.data.remote.WeatherApiService
import com.example.myweather.data.remote.WeatherRemoteDataSourceImpl
import com.example.myweather.data.remote.WeatherRequestException
import com.example.myweather.data.remote.dto.WeatherResponseDto
import com.example.myweather.domain.model.WeatherFailure
import java.net.UnknownHostException
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class WeatherRemoteDataSourceImplTest {

    @Test
    fun `losing connectivity is reported as NoInternet`() = runTest {
        val dataSource = WeatherRemoteDataSourceImpl(
            FakeWeatherApiService { throw UnknownHostException("wttr.in") },
        )

        assertEquals(
            WeatherFailure.NoInternet,
            failureFrom { dataSource.getCurrentWeather("Cairo") },
        )
    }

    @Test
    fun `an unknown city is recognised from the 500 body, not the status code`() = runTest {
        val dataSource = WeatherRemoteDataSourceImpl(
            FakeWeatherApiService {
                Response.error(500, LOCATION_NOT_FOUND_BODY.toResponseBody(PLAIN_TEXT))
            },
        )

        assertEquals(
            WeatherFailure.CityNotFound,
            failureFrom { dataSource.getCurrentWeather("zzzzzzzzzz") },
        )
    }

    @Test
    fun `any other server error is reported as ServerFailure`() = runTest {
        val dataSource = WeatherRemoteDataSourceImpl(
            FakeWeatherApiService {
                Response.error(503, "service unavailable".toResponseBody(PLAIN_TEXT))
            },
        )

        assertEquals(
            WeatherFailure.ServerFailure,
            failureFrom { dataSource.getCurrentWeather("Cairo") },
        )
    }

    @Test
    fun `a success with no body is reported as ServerFailure`() = runTest {
        val dataSource = WeatherRemoteDataSourceImpl(
            FakeWeatherApiService { Response.success(null) },
        )

        assertEquals(
            WeatherFailure.ServerFailure,
            failureFrom { dataSource.getCurrentWeather("Cairo") },
        )
    }

    private suspend fun failureFrom(block: suspend () -> Unit): WeatherFailure = try {
        block()
        throw AssertionError("Expected a WeatherRequestException")
    } catch (exception: WeatherRequestException) {
        exception.failure
    }

    private companion object {
        val PLAIN_TEXT = "text/plain; charset=utf-8".toMediaType()
        const val LOCATION_NOT_FOUND_BODY =
            "location not found: upstream error: opencage: invalid response"
    }
}

private class FakeWeatherApiService(
    private val respond: () -> Response<WeatherResponseDto>,
) : WeatherApiService {
    override suspend fun getCurrentWeather(city: String): Response<WeatherResponseDto> = respond()
}
