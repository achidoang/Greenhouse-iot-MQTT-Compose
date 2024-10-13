package com.kuliah.greenhouse_iot.data.local.monitoring

import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object MonitoringPreferences {
	val WATER_TEMP = intPreferencesKey("water_temp")  // Change to intPreferencesKey if necessary
	val WATER_PH = doublePreferencesKey("water_ph")   // Keep as double if needed
	val WATER_PPM = intPreferencesKey("water_ppm")
	val AIR_TEMP = intPreferencesKey("air_temp")
	val AIR_HUM = intPreferencesKey("air_hum")
}