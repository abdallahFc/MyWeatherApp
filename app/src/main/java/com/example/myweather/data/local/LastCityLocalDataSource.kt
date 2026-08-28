package com.example.myweather.data.local

interface LastCityLocalDataSource {
    suspend fun getLastSearchedCity(): String?
    suspend fun saveLastSearchedCity(city: String)
}
