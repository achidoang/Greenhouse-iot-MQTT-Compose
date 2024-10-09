package com.kuliah.greenhouse_iot.data.remote.mqtt

import android.content.Context
import android.util.Log
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MqttClientService @Inject constructor(
	@ApplicationContext private val context: Context
) {
	private val mqttClient: Mqtt3AsyncClient = MqttClient.builder()
		.useMqttVersion3()
		.serverHost("broker.emqx.io")
		.serverPort(1883)
		.identifier("aji111888") // Sesuaikan client ID
		.buildAsync()

	fun connect(onConnected: () -> Unit, onError: (Throwable?) -> Unit) {
		Log.d("MqttClientService", "Attempting to connect to MQTT broker")
		mqttClient.connect()
			.whenComplete { _, throwable ->
				if (throwable == null) {
					Log.d("MqttClientService", "Connected to MQTT broker successfully")
					onConnected()
				} else {
					Log.e("MqttClientService", "Failed to connect to MQTT broker: ${throwable.message}")
					onError(throwable)
				}
			}
	}

	fun subscribe(topic: String, qos: Int, messageListener: (String) -> Unit) {
		Log.d("MqttClientService", "Subscribing to topic: $topic")
		com.hivemq.client.mqtt.datatypes.MqttQos.fromCode(qos)?.let {
			mqttClient.subscribeWith()
				.topicFilter(topic)
				.qos(it)
				.callback { publish ->
					Log.d("MqttClientService", "Message received from topic $topic: ${String(publish.payloadAsBytes)}")
					messageListener(String(publish.payloadAsBytes))
				}
				.send()
				.whenComplete { _, throwable ->
					if (throwable == null) {
						Log.d("MqttClientService", "Successfully subscribed to topic: $topic")
					} else {
						Log.e("MqttClientService", "Failed to subscribe to topic: ${throwable.message}")
					}
				}
		}
	}

	fun disconnect() {
		Log.d("MqttClientService", "Disconnecting from MQTT broker")
		mqttClient.disconnect()
	}
}
