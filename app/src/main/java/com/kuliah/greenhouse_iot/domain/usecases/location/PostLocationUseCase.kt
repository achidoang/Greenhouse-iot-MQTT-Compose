package com.kuliah.greenhouse_iot.domain.usecases.location

import android.util.Log
import com.kuliah.greenhouse_iot.data.model.location.LocationRequest
import com.kuliah.greenhouse_iot.data.model.location.LocationResponse
import com.kuliah.greenhouse_iot.domain.repository.WeatherRepository
import javax.inject.Inject

class PostLocationUseCase @Inject constructor(private val repository: WeatherRepository) {
	suspend operator fun invoke(locationRequest: LocationRequest): LocationResponse {
		return repository.postLocation(locationRequest)
	}
}