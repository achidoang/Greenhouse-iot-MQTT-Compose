package com.kuliah.greenhouse_iot.data.local.monitoring

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MonitoringPreferencesManager @Inject constructor(
	private val dataStore: DataStore<Preferences>
) {

	suspend fun saveMonitoring(monitoringData: MonitoringData) {
		dataStore.edit { preferences ->
			preferences[MonitoringPreferences.WATER_TEMP] = monitoringData.watertemp.toInt() // Ensure type consistency
			preferences[MonitoringPreferences.WATER_PH] = monitoringData.waterph
			preferences[MonitoringPreferences.AIR_HUM] = monitoringData.airhum.toInt()
			preferences[MonitoringPreferences.AIR_TEMP] = monitoringData.airtemp.toInt()
			preferences[MonitoringPreferences.WATER_PPM] = monitoringData.waterppm.toInt()
		}
	}

	fun getMonitoring(): Flow<MonitoringData> {
		return dataStore.data.map { preferences ->
			MonitoringData(
				watertemp = preferences[MonitoringPreferences.WATER_TEMP]?.toDouble() ?: 1.0,  // Ensure correct casting
				waterppm = preferences[MonitoringPreferences.WATER_PPM]?.toDouble() ?: 1.0,
				waterph = preferences[MonitoringPreferences.WATER_PH] ?: 1.0,
				airtemp = preferences[MonitoringPreferences.AIR_TEMP]?.toDouble() ?: 1.0,
				airhum = preferences[MonitoringPreferences.AIR_HUM]?.toDouble() ?: 1.0
			)
		}
	}
}
