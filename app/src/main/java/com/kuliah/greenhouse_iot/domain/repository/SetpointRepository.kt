package com.kuliah.greenhouse_iot.domain.repository

import com.kuliah.greenhouse_iot.data.model.Setpoints

interface SetpointRepository {
	suspend fun getSetpoints(): Setpoints
	suspend fun publishSetpoints(setpoints: Setpoints): Result<Unit>
}
