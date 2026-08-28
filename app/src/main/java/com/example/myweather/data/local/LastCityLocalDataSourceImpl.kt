package com.example.myweather.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first

private val Context.weatherPreferences: DataStore<Preferences> by preferencesDataStore(
    name = "weather_preferences",
)

class LastCityLocalDataSourceImpl(
    private val context: Context,
) : LastCityLocalDataSource {

    override suspend fun getLastSearchedCity(): String? = context.weatherPreferences.data
        .catch { throwable -> if (throwable is IOException) emit(emptyPreferences()) else throw throwable }
        .first()[LAST_SEARCHED_CITY]
        ?.takeIf { it.isNotBlank() }

    override suspend fun saveLastSearchedCity(city: String) {
        context.weatherPreferences.edit { preferences -> preferences[LAST_SEARCHED_CITY] = city }
    }

    private companion object {
        val LAST_SEARCHED_CITY = stringPreferencesKey("last_searched_city")
    }
}
