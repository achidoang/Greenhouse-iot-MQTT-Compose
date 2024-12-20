package com.kuliah.greenhouse_iot.data.local.datastore

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONObject
import javax.inject.Inject

private val Context.authDataStore by preferencesDataStore(name = "auth_preferences")

class AuthDataStoreManager(private val context: Context) {

	companion object {
		private val TOKEN_KEY = stringPreferencesKey("auth_token")
		private val ROLE_KEY = stringPreferencesKey("user_role")
		private val USER_ID_KEY = stringPreferencesKey("user_id")
	}

	suspend fun saveAuthToken(token: String) {
		Log.d("AuthDataStore", "Saving token: $token")
		context.authDataStore.edit { preferences ->
			preferences[TOKEN_KEY] = token
		}
	}

	fun getAuthToken(): Flow<String?> {
		return context.authDataStore.data
			.map { preferences ->
				val token = preferences[TOKEN_KEY]
				Log.d("AuthDataStore", "Retrieved token: $token")
				token
			}
	}

	suspend fun clearAuthToken() {
		context.authDataStore.edit { preferences ->
			preferences.remove(TOKEN_KEY)
		}
	}

	// Menyimpan role pengguna
	suspend fun saveUserRole(role: String) {
		context.authDataStore.edit { preferences ->
			preferences[ROLE_KEY] = role
		}
	}

	// Membaca role pengguna
	fun getUserRole(): Flow<String?> {
		return context.authDataStore.data.map { preferences ->
			preferences[ROLE_KEY]
		}
	}

	fun isTokenValid(token: String?): Boolean {
		if (token.isNullOrEmpty()) return false

		return try {
			val parts = token.split(".")
			if (parts.size != 3) return false

			val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
			val jsonObject = JSONObject(payload)
			val exp = jsonObject.getLong("exp")
			val currentTime = System.currentTimeMillis() / 1000 // Waktu sekarang dalam detik
			currentTime < exp
		} catch (e: Exception) {
			false // Jika parsing gagal, anggap token tidak valid
		}
	}

	// Menyimpan user ID
	suspend fun saveUserId(userId: String) {
		context.authDataStore.edit { preferences ->
			preferences[USER_ID_KEY] = userId
		}
	}

	// Membaca user ID
	fun getUserId(): Flow<String?> {
		return context.authDataStore.data.map { preferences ->
			preferences[USER_ID_KEY]
		}
	}
}