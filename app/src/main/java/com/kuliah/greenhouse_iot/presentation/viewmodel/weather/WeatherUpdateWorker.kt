package com.kuliah.greenhouse_iot.presentation.viewmodel.weather

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kuliah.greenhouse_iot.data.local.datastore.WeatherDataStore
import com.kuliah.greenhouse_iot.domain.usecases.weather.GetWeatherUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@HiltWorker
class WeatherUpdateWorker @AssistedInject constructor(
	@Assisted appContext: Context,
	@Assisted workerParams: WorkerParameters,
	private val getWeatherUseCase: GetWeatherUseCase,
	private val weatherDataStore: WeatherDataStore
) : CoroutineWorker(appContext, workerParams) {

	override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
		try {
			val weather = getWeatherUseCase()
			weatherDataStore.saveWeather(weather)
			Log.d("WeatherUpdateWorker", "Weather updated successfully at ${System.currentTimeMillis()}")
			Result.success()
		} catch (e: Exception) {
			Log.e("WeatherUpdateWorker", "Error updating weather: ${e.message}")
			Result.retry()
		}
	}
}

