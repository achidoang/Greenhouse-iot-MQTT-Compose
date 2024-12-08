package com.kuliah.greenhouse_iot.domain.usecases.guide

import android.util.Log
import com.kuliah.greenhouse_iot.data.model.guide.Guide
import com.kuliah.greenhouse_iot.domain.repository.GuideRepository
import javax.inject.Inject

class GetGuidesUseCase @Inject constructor(
	private val repository: GuideRepository
) {
	suspend operator fun invoke(): List<Guide> {
		return try {
			val guides = repository.fetchGuidesFromApi()
			Log.d("GetGuidesUseCase", "Fetched from API: $guides")
			repository.saveGuidesToCache(guides)
			guides
		} catch (e: Exception) {
			Log.e("GetGuidesUseCase", "Error fetching from API, loading cache", e)
			val cachedGuides = repository.getCachedGuides()
			Log.d("GetGuidesUseCase", "Loaded from cache: $cachedGuides")
			cachedGuides
		}
	}
}
