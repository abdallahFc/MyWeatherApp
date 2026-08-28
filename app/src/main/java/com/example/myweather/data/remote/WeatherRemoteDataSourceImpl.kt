package com.example.myweather.data.remote

import com.example.myweather.data.remote.dto.WeatherResponseDto
import com.example.myweather.domain.model.WeatherFailure
import com.google.gson.JsonParseException
import java.io.IOException
import retrofit2.Response

class WeatherRemoteDataSourceImpl(
    private val weatherApiService: WeatherApiService,
) : WeatherRemoteDataSource {

    override suspend fun getCurrentWeather(city: String): WeatherResponseDto {
        val response = try {
            weatherApiService.getCurrentWeather(city)
        } catch (exception: IOException) {
            throw WeatherRequestException(WeatherFailure.NoInternet)
        } catch (exception: JsonParseException) {
            throw WeatherRequestException(WeatherFailure.ServerFailure)
        }

        if (!response.isSuccessful) throw WeatherRequestException(response.failureFromErrorBody())
        return response.body() ?: throw WeatherRequestException(WeatherFailure.ServerFailure)
    }

    private fun Response<*>.failureFromErrorBody(): WeatherFailure {
        val body = runCatching { errorBody()?.string() }.getOrNull().orEmpty()
        return if (body.contains(LOCATION_NOT_FOUND, ignoreCase = true)) {
            WeatherFailure.CityNotFound
        } else {
            WeatherFailure.ServerFailure
        }
    }

    private companion object {
        const val LOCATION_NOT_FOUND = "location not found"
    }
}
