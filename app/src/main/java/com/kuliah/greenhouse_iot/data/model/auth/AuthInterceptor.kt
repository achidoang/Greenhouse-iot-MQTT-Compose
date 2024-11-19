package com.kuliah.greenhouse_iot.data.model.auth

import com.kuliah.greenhouse_iot.data.local.datastore.AuthDataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor(private val authDataStoreManager: AuthDataStoreManager) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val requestBuilder = chain.request().newBuilder()

		// Ambil token secara sinkron dari DataStore
		val token = runBlocking { authDataStoreManager.getAuthToken().firstOrNull() }

		if (!token.isNullOrEmpty()) {
			requestBuilder.addHeader("Authorization", "Bearer $token")
		}

		val response = chain.proceed(requestBuilder.build())

		if (response.code == 401) {
			// Jika token tidak valid, lakukan logout
			runBlocking {
				authDataStoreManager.clearAuthToken()
			}
		}

		return response
	}
}
