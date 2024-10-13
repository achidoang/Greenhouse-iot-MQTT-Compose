package com.kuliah.greenhouse_iot.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface ConnectionRepository {
	suspend fun connectToBroker(): Result<Unit>
	suspend fun isConnected(): Boolean
}
