package com.kuliah.greenhouse_iot.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.kuliah.greenhouse_iot.data.local.SetpointPreferences
import com.kuliah.greenhouse_iot.data.model.Setpoints
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SetpointDataStoreManager @Inject constructor(
	private val dataStore: DataStore<Preferences>
) {

	suspend fun saveSetpoints(setpoints: Setpoints) {
		dataStore.edit { preferences ->
			preferences[SetpointPreferences.WATER_TEMP] = setpoints.waterTemp
			preferences[SetpointPreferences.WATER_PH] = setpoints.waterPh
			preferences[SetpointPreferences.WATER_PPM] = setpoints.waterPpm
			preferences[SetpointPreferences.AIR_TEMP] = setpoints.airTemp
			preferences[SetpointPreferences.AIR_HUM] = setpoints.airHum
		}
	}

	fun getSetpoint(): Flow<Setpoints>{
		return dataStore.data.map { preferences ->
			Setpoints(
				waterTemp = preferences[SetpointPreferences.WATER_TEMP] ?: 0,
				waterPh = preferences[SetpointPreferences.WATER_PH] ?: 7.9,
				waterPpm = preferences[SetpointPreferences.WATER_PPM] ?: 3000,
				airTemp = preferences[SetpointPreferences.AIR_TEMP] ?: 25,
				airHum = preferences[SetpointPreferences.AIR_HUM] ?: 50
			)
		}
	}
}
