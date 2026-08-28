package com.example.myweather.data.remote

import com.example.myweather.data.remote.dto.WeatherResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApiService {

    @GET("{city}?format=j1")
    suspend fun getCurrentWeather(@Path("city") city: String): Response<WeatherResponseDto>
}
