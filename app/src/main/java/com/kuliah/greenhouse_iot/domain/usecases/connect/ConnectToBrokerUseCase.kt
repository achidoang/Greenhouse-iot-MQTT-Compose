package com.kuliah.greenhouse_iot.domain.usecases.connect

import com.kuliah.greenhouse_iot.domain.repository.ConnectionRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ConnectToBrokerUseCase @Inject constructor(
	private val repository: ConnectionRepository
) {
	suspend operator fun invoke(): Result<Unit> {
		return repository.connectToBroker()
	}
}

