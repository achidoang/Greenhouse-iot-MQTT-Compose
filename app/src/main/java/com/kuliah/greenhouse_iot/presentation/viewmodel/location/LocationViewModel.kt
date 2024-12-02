//package com.kuliah.greenhouse_iot.presentation.viewmodel.location
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.util.Log
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.kuliah.greenhouse_iot.data.model.location.LocationResponse
//import com.kuliah.greenhouse_iot.domain.usecases.location.GetLocationUseCase
//import com.kuliah.greenhouse_iot.domain.usecases.location.PostLocationUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//import javax.inject.Inject
//
//@HiltViewModel
//class LocationViewModel @Inject constructor(
//	private val getLocationUseCase: GetLocationUseCase,
//	private val postLocationUseCase: PostLocationUseCase,
//	private val fusedLocationClient: FusedLocationProviderClient,
//	@ApplicationContext private val context: Context // Inject the application context
//) : ViewModel() {
//
//	private val _location = MutableStateFlow<LocationResponse?>(null)
//	val location: StateFlow<LocationResponse?> = _location
//
//	fun fetchLocation() {
//		if (ContextCompat.checkSelfPermission(
//				context,
//				Manifest.permission.ACCESS_FINE_LOCATION
//			) == PackageManager.PERMISSION_GRANTED
//		) {
//			viewModelScope.launch {
//				try {
//					val locationResult = fusedLocationClient.lastLocation.await()
//					if (locationResult != null) {
//						Log.d("LocationViewModel", "Fetched location: ${locationResult.latitude}, ${locationResult.longitude}")
//						saveLocation(locationResult.latitude, locationResult.longitude)
//					} else {
//						Log.e("LocationViewModel", "Failed to fetch location: Location result is null")
//					}
//				} catch (e: Exception) {
//					Log.e("LocationViewModel", "Error fetching location: ${e.message}")
//				}
//			}
//		} else {
//			Log.e("LocationViewModel", "Permission not granted for location access")
//		}
//	}
//
//
//	fun saveLocation(latitude: Double, longitude: Double) {
//		viewModelScope.launch {
//			try {
//				Log.d("LocationViewModel", "Saving location: $latitude, $longitude")
//				val response = postLocationUseCase(latitude, longitude)
//				Log.d("LocationViewModel", "Location saved successfully: ${response.latitude}, ${response.longitude}")
//				_location.value = response
//			} catch (e: Exception) {
//				Log.e("LocationViewModel", "Error saving location: ${e.message}")
//			}
//		}
//	}
//
//}
//
