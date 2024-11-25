package com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt

import android.util.Log
import com.kuliah.greenhouse_iot.domain.repository.MqttRepository
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

//class SubscribeAktuatorUseCase @Inject constructor(
//	private val repository: MqttRepository
//) {
//	operator fun invoke(): Flow<AktuatorData> = repository.subscribeAktuator()
//}

class SubscribeAktuatorUseCase @Inject constructor(
	private val mqttRepository: MqttRepository
) {
	operator fun invoke() = mqttRepository.subscribeAktuator()
		.onEach { data ->
			Log.d("SubscribeAktuatorUseCase", "Aktuator Data received in UseCase: $data")
		}
}

