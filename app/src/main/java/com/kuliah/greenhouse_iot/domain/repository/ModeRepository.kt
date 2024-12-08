package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.mode.ModeResponse

interface ModeRepository {
	suspend fun postMode(automode: Int): Result<ModeResponse>
}
