package com.example.myweather.di

import android.content.Context
import com.example.myweather.data.local.LastCityLocalDataSource
import com.example.myweather.data.local.LastCityLocalDataSourceImpl
import com.example.myweather.data.remote.WeatherApiService
import com.example.myweather.data.remote.WeatherRemoteDataSource
import com.example.myweather.data.remote.WeatherRemoteDataSourceImpl
import com.example.myweather.data.repository.WeatherRepositoryImpl
import com.example.myweather.domain.repository.WeatherRepository
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(context: Context) {

    private val applicationContext = context.applicationContext

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    private val weatherApiService: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(WTTR_IN_BASE_URL)
            .client(okHttpClient)

            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    private val weatherRemoteDataSource: WeatherRemoteDataSource by lazy {
        WeatherRemoteDataSourceImpl(weatherApiService)
    }

    private val lastCityLocalDataSource: LastCityLocalDataSource by lazy {
        LastCityLocalDataSourceImpl(applicationContext)
    }

    val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(weatherRemoteDataSource, lastCityLocalDataSource)
    }

    private companion object {
        const val WTTR_IN_BASE_URL = "https://wttr.in/"
        const val REQUEST_TIMEOUT_SECONDS = 10L
    }
}
