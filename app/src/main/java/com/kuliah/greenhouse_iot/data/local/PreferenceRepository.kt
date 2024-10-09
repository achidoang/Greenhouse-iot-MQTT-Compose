package com.kuliah.greenhouse_iot.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.kuliah.greenhouse_iot.util.Constants.THEME_MODE_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceRepository @Inject constructor (
	private val dataStore: DataStore<Preferences>
) {
	val storedThemeModel: Flow<Boolean> = dataStore.data.map {
		it[THEME_MODE_KEY] ?: false
	}.distinctUntilChanged()

	suspend fun storeThemeMode( themeMode: Boolean ) {
		dataStore.edit {
			it[THEME_MODE_KEY] = themeMode
		}
	}

}