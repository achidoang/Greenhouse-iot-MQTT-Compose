package com.kuliah.greenhouse_iot.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.kuliah.greenhouse_iot.data.model.weather.WeatherResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherDataStore @Inject constructor(
	private val dataStore: DataStore<Preferences>,
	private val gson: Gson // Pastikan Anda punya dependensi Gson
) {

	private val WEATHER_KEY = stringPreferencesKey("weather_data")

	fun getWeather(): Flow<WeatherResponse?> {
		return dataStore.data
			.catch { e -> Log.e("WeatherDataStore", "Error: ${e.message}") }
			.map { preferences ->
				val json = preferences[WEATHER_KEY]
				json?.let { gson.fromJson(it, WeatherResponse::class.java) }
			}
	}

	suspend fun saveWeather(weather: WeatherResponse) {
		dataStore.edit { preferences ->
			preferences[WEATHER_KEY] = gson.toJson(weather)
		}
	}
}