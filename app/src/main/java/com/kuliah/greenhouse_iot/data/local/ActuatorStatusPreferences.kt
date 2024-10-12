package com.kuliah.greenhouse_iot.data.local

import androidx.datastore.preferences.core.intPreferencesKey

object ActuatorStatusPreferences {
	val ACTUATOR_NUTRISI = intPreferencesKey("actuator_nutrisi")
	val ACTUATOR_PH_UP = intPreferencesKey("actuator_ph_up")
	val ACTUATOR_PH_DOWN = intPreferencesKey("actuator_ph_down")
	val ACTUATOR_AIR_BAKU = intPreferencesKey("actuator_air_baku")
	val ACTUATOR_POMPA_UTAMA_1 = intPreferencesKey("actuator_pompa_utama_1")
	val ACTUATOR_POMPA_UTAMA_2 = intPreferencesKey("actuator_pompa_utama_2")
}
