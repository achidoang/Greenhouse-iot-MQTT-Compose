package com.kuliah.greenhouse_iot.data.remote.api.guide

import com.kuliah.greenhouse_iot.data.model.guide.Guide
import retrofit2.http.GET

interface GuideApi {
	@GET("api/v2/guides/")
	suspend fun getGuides(): List<Guide>
}