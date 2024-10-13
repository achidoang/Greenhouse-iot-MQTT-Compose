package com.kuliah.greenhouse_iot.data.remote.mqtt

import android.content.Context
import android.util.Log
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.kuliah.greenhouse_iot.data.model.ActuatorStatus
import com.kuliah.greenhouse_iot.data.model.Setpoints
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject

class MqttClientService @Inject constructor(
	@ApplicationContext private val context: Context
) {
	private val mqttClient: Mqtt3AsyncClient = MqttClient.builder()
		.useMqttVersion3()
		.serverHost("44.232.241.40")
		.serverPort(1883)
		.identifier("aji111") // Sesuaikan client ID
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

	fun isConnected(): Boolean {
		return mqttClient.state.isConnected
	}


	// Fetch the latest setpoints from the MQTT topic 'herbalawu/setpoint'
	fun getSetpointsFromMqtt(onSetpointsReceived: (Setpoints) -> Unit, onError: (Throwable?) -> Unit) {
		subscribe("herbalawu/setpoint", 1) { message ->
			try {
				val setpoints = parseSetpoints(message)
				onSetpointsReceived(setpoints)
			} catch (e: Exception) {
				Log.e("MqttClientService", "Failed to parse setpoints: ${e.message}")
				onError(e)
			}
		}
	}

	// Publish setpoints to the 'herbalawu/setpoint' topic
	fun publishSetpointsToMqtt(setpoints: Setpoints, onSuccess: () -> Unit, onError: (Throwable?) -> Unit) {
		val payload = serializeSetpoints(setpoints)
		mqttClient.publishWith()
			.topic("herbalawu/setpoint")
			.payload(payload.toByteArray())
			.send()
			.whenComplete { _, throwable ->
				if (throwable != null) {
					Log.e("MqttClientService", "Failed to publish setpoints: ${throwable.message}")
					onError(throwable)
				} else {
					Log.d("MqttClientService", "Setpoints published successfully")
					onSuccess()
				}
			}
	}

	// Helper function to parse the MQTT message and convert to Setpoints
	private fun parseSetpoints(message: String): Setpoints {
		val json = JSONObject(message)
		return Setpoints(
			waterTemp = json.getInt("watertemp"),
			waterPh = json.getDouble("waterph"),
			waterPpm = json.getInt("waterppm"),
			airTemp = json.getInt("airtemp"),
			airHum = json.getInt("airhum")
		)
	}

	// Helper function to serialize Setpoints into JSON string
	private fun serializeSetpoints(setpoints: Setpoints): String {
		val json = JSONObject()
		json.put("watertemp", setpoints.waterTemp)
		json.put("waterph", setpoints.waterPh)
		json.put("waterppm", setpoints.waterPpm)
		json.put("airtemp", setpoints.airTemp)
		json.put("airhum", setpoints.airHum)
		return json.toString()
	}

	// Kontrol manual
	// Fetch the latest actuator status from the MQTT topic 'herbalawu/aktuator'
	fun getActuatorStatusFromMqtt(onStatusReceived: (ActuatorStatus) -> Unit, onError: (Throwable?) -> Unit) {
		subscribe("herbalawu/aktuator", 1) { message ->
			try {
				val actuatorStatus = parseActuatorStatus(message)
				onStatusReceived(actuatorStatus)
			} catch (e: Exception) {
				Log.e("MqttClientService", "Failed to parse actuator status: ${e.message}")
				onError(e)
			}
		}
	}

	// Publish actuator status to the 'herbalawu/aktuator' topic
	fun publishActuatorStatusToMqtt(actuatorStatus: ActuatorStatus, onSuccess: () -> Unit, onError: (Throwable?) -> Unit) {
		val payload = serializeActuatorStatus(actuatorStatus)
		mqttClient.publishWith()
			.topic("herbalawu/aktuator")
			.payload(payload.toByteArray())
			.send()
			.whenComplete { _, throwable ->
				if (throwable != null) {
					Log.e("MqttClientService", "Failed to publish actuator status: ${throwable.message}")
					onError(throwable)
				} else {
					Log.d("MqttClientService", "Actuator status published successfully")
					onSuccess()
				}
			}
	}

	// Helper function to parse the MQTT message and convert to ActuatorStatus
	private fun parseActuatorStatus(message: String): ActuatorStatus {
		val json = JSONObject(message)
		return ActuatorStatus(
			actuator_nutrisi = json.getInt("actuator_nutrisi"),
			actuator_ph_up = json.getInt("actuator_ph_up"),
			actuator_ph_down = json.getInt("actuator_ph_down"),
			actuator_air_baku = json.getInt("actuator_air_baku"),
			actuator_pompa_utama_1 = json.getInt("actuator_pompa_utama_1"),
			actuator_pompa_utama_2 = json.getInt("actuator_pompa_utama_2")
		)
	}

	// Helper function to serialize ActuatorStatus into JSON string
	private fun serializeActuatorStatus(actuatorStatus: ActuatorStatus): String {
		val json = JSONObject()
		json.put("actuator_nutrisi", actuatorStatus.actuator_nutrisi)
		json.put("actuator_ph_up", actuatorStatus.actuator_ph_up)
		json.put("actuator_ph_down", actuatorStatus.actuator_ph_down)
		json.put("actuator_air_baku", actuatorStatus.actuator_air_baku)
		json.put("actuator_pompa_utama_1", actuatorStatus.actuator_pompa_utama_1)
		json.put("actuator_pompa_utama_2", actuatorStatus.actuator_pompa_utama_2)
		return json.toString()
	}





	fun publish(topic: String, message: String) {
		mqttClient.publishWith()
			.topic(topic)
			.payload(message.toByteArray())
			.send()
			.whenComplete { _, throwable ->
				if (throwable != null) {
					Log.e("MqttClientService", "Failed to publish message: ${throwable.message}")
				} else {
					Log.d("MqttClientService", "Message published to topic $topic")
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
