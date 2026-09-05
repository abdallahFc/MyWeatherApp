package com.example.myweather.data.repository

import com.example.myweather.data.local.LastCityLocalDataSource
import com.example.myweather.data.mapper.WeatherDataMapper
import com.example.myweather.data.remote.WeatherRemoteDataSource
import com.example.myweather.data.remote.WeatherRequestException
import com.example.myweather.domain.model.WeatherFailure
import com.example.myweather.domain.model.WeatherResult
import com.example.myweather.domain.repository.WeatherRepository
import kotlin.coroutines.cancellation.CancellationException

class WeatherRepositoryImpl(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val lastCityLocalDataSource: LastCityLocalDataSource,
) : WeatherRepository {

    override suspend fun getCurrentWeather(city: String): WeatherResult = try {
        val response = weatherRemoteDataSource.getCurrentWeather(city)
        when (val weather = WeatherDataMapper.toDomain(response, requestedCity = city)) {
            null -> WeatherResult.Failure(WeatherFailure.ServerFailure)
            else -> {
                lastCityLocalDataSource.saveLastSearchedCity(weather.cityName)
                WeatherResult.Success(weather)
            }
        }
    } catch (exception: WeatherRequestException) {
        WeatherResult.Failure(exception.failure)
    } catch (_: CancellationException) {
        WeatherResult.Failure(WeatherFailure.Unknown)
    } catch (_: Exception) {
        WeatherResult.Failure(WeatherFailure.Unknown)
    }

    override suspend fun getLastSearchedCity(): String? =
        lastCityLocalDataSource.getLastSearchedCity()
}
