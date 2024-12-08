package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.guide.Guide

interface GuideRepository {
	suspend fun fetchGuidesFromApi(): List<Guide>
	suspend fun getCachedGuides(): List<Guide>
	suspend fun saveGuidesToCache(guides: List<Guide>)
}
