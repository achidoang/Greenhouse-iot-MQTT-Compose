package com.kuliah.greenhouse_iot.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.kuliah.greenhouse_iot.data.model.ActuatorStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ActuatorPreferencesManager @Inject constructor(
	private val dataStore: DataStore<Preferences>
) {

	suspend fun saveActuatorStatus(actuatorStatus: ActuatorStatus) {
		dataStore.edit { preferences ->
			preferences[ActuatorStatusPreferences.ACTUATOR_NUTRISI] = actuatorStatus.actuator_nutrisi
			preferences[ActuatorStatusPreferences.ACTUATOR_PH_UP] = actuatorStatus.actuator_ph_up
			preferences[ActuatorStatusPreferences.ACTUATOR_PH_DOWN] = actuatorStatus.actuator_ph_down
			preferences[ActuatorStatusPreferences.ACTUATOR_AIR_BAKU] = actuatorStatus.actuator_air_baku
			preferences[ActuatorStatusPreferences.ACTUATOR_POMPA_UTAMA_1] = actuatorStatus.actuator_pompa_utama_1
			preferences[ActuatorStatusPreferences.ACTUATOR_POMPA_UTAMA_2] = actuatorStatus.actuator_pompa_utama_2
		}
	}

	fun getActuatorStatus(): Flow<ActuatorStatus> {
		return dataStore.data.map { preferences ->
			ActuatorStatus(
				actuator_nutrisi = preferences[ActuatorStatusPreferences.ACTUATOR_NUTRISI] ?: 0,
				actuator_ph_up = preferences[ActuatorStatusPreferences.ACTUATOR_PH_UP] ?: 0,
				actuator_ph_down = preferences[ActuatorStatusPreferences.ACTUATOR_PH_DOWN] ?: 0,
				actuator_air_baku = preferences[ActuatorStatusPreferences.ACTUATOR_AIR_BAKU] ?: 0,
				actuator_pompa_utama_1 = preferences[ActuatorStatusPreferences.ACTUATOR_POMPA_UTAMA_1] ?: 0,
				actuator_pompa_utama_2 = preferences[ActuatorStatusPreferences.ACTUATOR_POMPA_UTAMA_2] ?: 0
			)
		}
	}
}
