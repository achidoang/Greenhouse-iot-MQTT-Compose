package com.kuliah.greenhouse_iot.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("mode_preferences")

class ModePreferences(private val context: Context) {
	private val IS_AUTOMATIC_MODE = booleanPreferencesKey("is_automatic_mode")

	val isAutomaticMode: Flow<Boolean>
		get() = context.dataStore.data.map { preferences ->
			preferences[IS_AUTOMATIC_MODE] ?: true // Default otomatis
		}

	suspend fun setAutomaticMode(isAutomatic: Boolean) {
		context.dataStore.edit { preferences ->
			preferences[IS_AUTOMATIC_MODE] = isAutomatic
		}
	}
}
