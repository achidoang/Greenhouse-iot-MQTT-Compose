package com.kuliah.greenhouse_iot.data.local.setpoint

import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object SetpointPreferences {
	val WATER_TEMP = intPreferencesKey("water_temp")
	val WATER_PH = doublePreferencesKey("water_ph")
	val WATER_PPM = intPreferencesKey("water_ppm")
	val AIR_TEMP = intPreferencesKey("air_temp")
	val AIR_HUM = intPreferencesKey("air_hum")
}