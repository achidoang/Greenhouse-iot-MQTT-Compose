package com.kuliah.greenhouse_iot.data.repository

import android.util.Log
import com.google.gson.Gson
import com.kuliah.greenhouse_iot.data.local.datastore.DataStoreManager
import com.kuliah.greenhouse_iot.data.local.datastore.GuideDataStoreManager
import com.kuliah.greenhouse_iot.data.model.guide.Guide
import com.kuliah.greenhouse_iot.data.remote.api.guide.GuideApi
import com.kuliah.greenhouse_iot.domain.repository.GuideRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

class GuideRepositoryImpl @Inject constructor(
	private val api: GuideApi,
	private val dataStoreManager: GuideDataStoreManager
) : GuideRepository {

	init {
		Log.d("GuideRepositoryImpl", "GuideApi: $api, DataStoreManager: $dataStoreManager")
	}

	override suspend fun fetchGuidesFromApi(): List<Guide> {
		return try {
			val guides = api.getGuides()
			Log.d("GuideRepositoryImpl", "Fetched guides from API: $guides")
			guides
		} catch (e: Exception) {
			Log.e("GuideRepositoryImpl", "Error fetching guides: ${e.message}", e)
			emptyList()
		}
	}

	override suspend fun saveGuidesToCache(guides: List<Guide>) {
		try {
			dataStoreManager.saveGuides(guides)
			Log.d("GuideRepositoryImpl", "Saved guides to cache: $guides")
		} catch (e: Exception) {
			Log.e("GuideRepositoryImpl", "Error saving guides to cache: ${e.message}", e)
		}
	}

	override suspend fun getCachedGuides(): List<Guide> {
		val cachedGuides = dataStoreManager.guidesFlow.firstOrNull() ?: emptyList()
		Log.d("GuideRepositoryImpl", "Cached guides: $cachedGuides")
		return cachedGuides
	}


}
