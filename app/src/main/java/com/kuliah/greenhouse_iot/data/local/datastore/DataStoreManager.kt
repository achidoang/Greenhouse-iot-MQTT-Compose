package com.kuliah.greenhouse_iot.data.local.datastore

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kuliah.greenhouse_iot.data.model.subscribe.AktuatorData
import com.kuliah.greenhouse_iot.data.model.subscribe.MonitoringData
import com.kuliah.greenhouse_iot.data.model.subscribe.SetPointData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeParseException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "iot_data_store")

class DataStoreManager @Inject constructor(private val context: Context) {

	private val dataStore = context.dataStore

	companion object {
		val TIMESTAMP_KEY = stringPreferencesKey("timestamp")
		val WATERTEMP_KEY = floatPreferencesKey("watertemp")
		val WATERPPM_KEY = floatPreferencesKey("waterppm")
		val WATERPH_KEY = floatPreferencesKey("waterph")
		val AIRTEMP_KEY = floatPreferencesKey("airtemp")
		val AIRHUM_KEY = floatPreferencesKey("airhum")

		// Key untuk data aktuator
		val AKTUATOR_TIMESTAMP_KEY = stringPreferencesKey("aktuator_timestamp")
		val AKTUATOR_ID_KEY = intPreferencesKey("aktuator_id")
		val NUTRISI_KEY = booleanPreferencesKey("actuator_nutrisi")
		val PH_UP_KEY = booleanPreferencesKey("actuator_ph_up")
		val PH_DOWN_KEY = booleanPreferencesKey("actuator_ph_down")
		val AIR_BAKU_KEY = booleanPreferencesKey("actuator_air_baku")
		val POMPA_UTAMA1_KEY = booleanPreferencesKey("actuator_pompa_utama1")
		val POMPA_UTAMA2_KEY = booleanPreferencesKey("actuator_pompa_utama2")

		// Key untuk data setpoint
		val SETPOINT_TIMESTAMP_KEY = stringPreferencesKey("setpoint_timestamp")
		val SETPOINT_WATERTEMP_KEY = floatPreferencesKey("setpoint_watertemp")
		val SETPOINT_WATERPPM_KEY = floatPreferencesKey("setpoint_waterppm")
		val SETPOINT_WATERPH_KEY = floatPreferencesKey("setpoint_waterph")
		val SETPOINT_PROFILE_KEY = stringPreferencesKey("setpoint_profile")
		val SETPOINT_STATUS = stringPreferencesKey("setpoint_status")

	}

	@RequiresApi(Build.VERSION_CODES.O)
	suspend fun saveMonitoringData(data: MonitoringData) {
		context.dataStore.edit { preferences ->
			val currentTimestamp = Instant.now()
			try {
				val timestamp = Instant.parse(data.timestamp)
				val duration = Duration.between(timestamp, currentTimestamp).toHours()

				// Hanya simpan data jika durasinya kurang dari 5 jam
				if (duration <= 5) {
					preferences[TIMESTAMP_KEY] = data.timestamp
					preferences[WATERTEMP_KEY] = data.watertemp
					preferences[WATERPPM_KEY] = data.waterppm
					preferences[WATERPH_KEY] = data.waterph
					preferences[AIRTEMP_KEY] = data.airtemp
					preferences[AIRHUM_KEY] = data.airhum
					Log.d("DataStoreManager", "Data saved: $data")
				} else {
					preferences.clear()
					Log.d("DataStoreManager", "Old data cleared")
				}
			} catch (e: DateTimeParseException) {
				Log.e("DataStoreManager", "Invalid timestamp, skipping save", e)
			}
		}
	}



	@RequiresApi(Build.VERSION_CODES.O)
	fun getMonitoringData(): Flow<MonitoringData?> {
		return context.dataStore.data.map { preferences ->
			val timestamp = preferences[TIMESTAMP_KEY]
			Log.d("DataStoreManager", "Timestamp: $timestamp, WaterTemp: ${preferences[WATERTEMP_KEY]}")
			if (timestamp != null) {
				MonitoringData(
					watertemp = preferences[WATERTEMP_KEY] ?: 0f,
					waterppm = preferences[WATERPPM_KEY] ?: 0f,
					waterph = preferences[WATERPH_KEY] ?: 0f,
					airtemp = preferences[AIRTEMP_KEY]
						?: 0f, // Tambahkan key dan parsing sesuai kebutuhan
					airhum = preferences[AIRHUM_KEY] ?: 0f,
					timestamp = timestamp
				)
			} else {
				null
			}
		}
	}

	// Fungsi untuk menyimpan data aktuator
	@RequiresApi(Build.VERSION_CODES.O)
	suspend fun saveAktuatorData(data: AktuatorData) {
		context.dataStore.edit { preferences ->
			preferences[AKTUATOR_TIMESTAMP_KEY] = data.timestamp
//			preferences[AKTUATOR_ID_KEY] = data.id
			preferences[NUTRISI_KEY] = data.actuator_nutrisi
			preferences[PH_UP_KEY] = data.actuator_ph_up
			preferences[PH_DOWN_KEY] = data.actuator_ph_down
			preferences[AIR_BAKU_KEY] = data.actuator_air_baku
			preferences[POMPA_UTAMA1_KEY] = data.actuator_pompa_utama_1
			preferences[POMPA_UTAMA2_KEY] = data.actuator_pompa_utama_2
			Log.d("DataStoreManager", "Aktuator data saved: $data")
		}
	}

	// Fungsi untuk mengambil data aktuator
	@RequiresApi(Build.VERSION_CODES.O)
	fun getAktuatorData(): Flow<AktuatorData?> {
		return context.dataStore.data.map { preferences ->
			val timestamp = preferences[AKTUATOR_TIMESTAMP_KEY]
			if (timestamp != null) {
				AktuatorData(
					timestamp = timestamp,
//					id = preferences[AKTUATOR_ID_KEY] ?: 0,
					actuator_nutrisi = preferences[NUTRISI_KEY] ?: false,
					actuator_ph_up = preferences[PH_UP_KEY] ?: false,
					actuator_ph_down = preferences[PH_DOWN_KEY] ?: false,
					actuator_air_baku = preferences[AIR_BAKU_KEY] ?: false,
					actuator_pompa_utama_1 = preferences[POMPA_UTAMA1_KEY] ?: false,
					actuator_pompa_utama_2 = preferences[POMPA_UTAMA2_KEY] ?: false
				)
			} else {
				null
			}
		}
	}

	// Fungsi untuk menyimpan data setpoint
	@RequiresApi(Build.VERSION_CODES.O)
	suspend fun saveSetPointData(data: SetPointData) {
		context.dataStore.edit { preferences ->
			preferences[SETPOINT_TIMESTAMP_KEY] = data.timestamp
//			preferences[SETPOINT_ID_KEY] = data.id
			preferences[SETPOINT_WATERTEMP_KEY] = data.watertemp
			preferences[SETPOINT_WATERPPM_KEY] = data.waterppm
			preferences[SETPOINT_WATERPH_KEY] = data.waterph
			preferences[SETPOINT_PROFILE_KEY] = data.profile
			Log.d("DataStoreManager", "SetPoint data saved: $data")
		}
	}

	// Fungsi untuk mengambil data setpoint
	@RequiresApi(Build.VERSION_CODES.O)
	fun getSetPointData(): Flow<SetPointData?> {
		return context.dataStore.data.map { preferences ->
			val timestamp = preferences[SETPOINT_TIMESTAMP_KEY]
			if (timestamp != null) {
				SetPointData(
					timestamp = timestamp,
					//					id = preferences[SETPOINT_ID_KEY] ?: 0,
					watertemp = preferences[SETPOINT_WATERTEMP_KEY] ?: 0f,
					waterppm = preferences[SETPOINT_WATERPPM_KEY] ?: 0f,
					waterph = preferences[SETPOINT_WATERPH_KEY] ?: 0f,
					profile = preferences[SETPOINT_PROFILE_KEY] ?: "",
					status = preferences[SETPOINT_STATUS] ?: "",
				)
			} else {
				null
			}
		}
	}

	@RequiresApi(Build.VERSION_CODES.O)
	fun getMonitoringDataHistory(): Flow<List<MonitoringData>> = flow {
		val dataList = mutableListOf<MonitoringData>()
		context.dataStore.data.collect { preferences ->
			val timestamp = preferences[TIMESTAMP_KEY]
			if (timestamp != null) {
				val data = MonitoringData(
					watertemp = preferences[WATERTEMP_KEY] ?: 0f,
					waterppm = preferences[WATERPPM_KEY] ?: 0f,
					waterph = preferences[WATERPH_KEY] ?: 0f,
					airtemp = preferences[AIRTEMP_KEY] ?: 0f,
					airhum = preferences[AIRHUM_KEY] ?: 0f,
					timestamp = timestamp
				)
				dataList.add(data)
			}
		}
		emit(dataList)
	}

	@RequiresApi(Build.VERSION_CODES.O)
	fun getAktuatorDataHistory(): Flow<List<AktuatorData>> = flow {
		val dataList = mutableListOf<AktuatorData>()
		context.dataStore.data.collect { preferences ->
			val timestamp = preferences[AKTUATOR_TIMESTAMP_KEY]
			if (timestamp != null) {
				val data = AktuatorData(
					actuator_nutrisi = preferences[NUTRISI_KEY] ?: false,
					actuator_ph_down = preferences[PH_DOWN_KEY] ?: false,
					actuator_ph_up = preferences[PH_UP_KEY] ?: false,
					actuator_air_baku = preferences[AIR_BAKU_KEY] ?: false,
					actuator_pompa_utama_1 = preferences[POMPA_UTAMA1_KEY] ?: false,
					actuator_pompa_utama_2 = preferences[POMPA_UTAMA2_KEY] ?: false,
					timestamp = timestamp
				)
				dataList.add(data)
			}
		}
		emit(dataList)
	}

	@RequiresApi(Build.VERSION_CODES.O)
	fun getSetPointDataHistory(): Flow<List<SetPointData>> = flow {
		val dataList = mutableListOf<SetPointData>()
		context.dataStore.data.collect { preferences ->
			val timestamp = preferences[SETPOINT_TIMESTAMP_KEY]
			if (timestamp != null) {
				val data = SetPointData(
					timestamp = timestamp,
					watertemp = preferences[SETPOINT_WATERTEMP_KEY] ?: 0f,
					waterppm = preferences[SETPOINT_WATERPPM_KEY] ?: 0f,
					waterph = preferences[SETPOINT_WATERPH_KEY] ?: 0f,
					profile = preferences[SETPOINT_PROFILE_KEY] ?: "",
					status = preferences[SETPOINT_STATUS] ?: "",
				)
				dataList.add(data)
			}
		}
		emit(dataList)
	}


}