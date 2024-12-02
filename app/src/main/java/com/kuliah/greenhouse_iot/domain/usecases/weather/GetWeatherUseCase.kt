package com.kuliah.greenhouse_iot.domain.usecases.weather

import com.kuliah.greenhouse_iot.data.model.weather.WeatherResponse
import com.kuliah.greenhouse_iot.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(private val repository: WeatherRepository) {
	suspend operator fun invoke(): WeatherResponse {
		return repository.getWeather()
	}
}
