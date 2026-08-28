package com.example.myweather

import android.app.Application
import com.example.myweather.di.AppContainer

class MyWeatherApplication : Application() {
    val appContainer: AppContainer by lazy { AppContainer(this) }
}
