package com.kuliah.greenhouse_iot.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.kuliah.greenhouse_iot.data.local.datastore.DataStoreManager
import com.kuliah.greenhouse_iot.data.model.AktuatorData
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.data.model.SetPointData
import com.kuliah.greenhouse_iot.domain.repository.MqttRepository
import com.kuliah.greenhouse_iot.util.ConnectionStatus
import com.kuliah.greenhouse_iot.util.Constants.WEBSOCKET_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.Response
import org.json.JSONObject
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MqttRepositoryImpl @Inject constructor(
	private val okHttpClient: OkHttpClient,
	private val dataStoreManager: DataStoreManager
) : MqttRepository {

	private var webSocket: WebSocket? = null
	private val _connectionStatus = MutableStateFlow(ConnectionStatus.DISCONNECTED)
	override fun getConnectionStatus(): Flow<ConnectionStatus> = _connectionStatus.asStateFlow()

	override suspend fun reconnect() {
		while (_connectionStatus.value != ConnectionStatus.CONNECTED) {
			delay(5000)
			openWebSocketConnection()
		}
	}


	private val retryFlow = MutableSharedFlow<Unit>(replay = 0)

	private fun retryWebSocket() {
		_connectionStatus.value = ConnectionStatus.DISCONNECTED
		CoroutineScope(Dispatchers.IO).launch {
			retryFlow.emit(Unit)
		}
	}

	private fun observeRetryFlow() {
		CoroutineScope(Dispatchers.IO).launch {
			retryFlow.collect {
				while (_connectionStatus.value != ConnectionStatus.CONNECTED) {
					delay(5000) // Tunggu 5 detik sebelum mencoba ulang
					openWebSocketConnection()
				}
			}
		}
	}

	// Di dalam init block
	init {
		openWebSocketConnection()
		observeRetryFlow()
	}

	@RequiresApi(Build.VERSION_CODES.O)
	override fun subscribeMonitoring(): Flow<MonitoringData> = flow {
		dataStoreManager.getMonitoringData().collect { cachedData ->
			if (cachedData != null) emit(cachedData) // Emit data dari DataStore jika ada
		}
		openWebSocketConnection()
	}

	@RequiresApi(Build.VERSION_CODES.O)
	override fun subscribeAktuator(): Flow<AktuatorData> = flow {
		var lastEmittedData: AktuatorData? = null
		dataStoreManager.getAktuatorData().collect { cachedData ->
			if (cachedData != null && cachedData != lastEmittedData) {
				lastEmittedData = cachedData
				Log.d("MqttRepositoryImpl", "Emitting new AktuatorData: $cachedData")
				emit(cachedData)
			}
		}
		openWebSocketConnection()
	}



	@RequiresApi(Build.VERSION_CODES.O)
	override fun subscribeSetPoint(): Flow<SetPointData> = flow {
		var lastEmittedData: SetPointData? = null
		dataStoreManager.getSetPointData().collect { cachedData ->
			if (cachedData != null && cachedData != lastEmittedData) {
				lastEmittedData = cachedData
				Log.d("MqttRepositoryImpl", "Emitting new SetPointData: $cachedData")
				emit(cachedData)
			}
		}
		openWebSocketConnection()
	}

	private fun openWebSocketConnection() {
		val request = Request.Builder().url(WEBSOCKET_URL).build()

		webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
			override fun onOpen(webSocket: WebSocket, response: Response) {
				_connectionStatus.value = ConnectionStatus.CONNECTED
				Log.d("MqttRepositoryImpl", "WebSocket connection opened")
				sendHeartbeat()
			}

			@RequiresApi(Build.VERSION_CODES.O)
			override fun onMessage(webSocket: WebSocket, text: String) {
				Log.d("MqttRepositoryImpl", "Received message: $text")
				when {
					text.contains("herbalawu/monitoring") -> parseAndSaveMonitoringData(text)
					text.contains("herbalawu/aktuator") -> parseAndSaveAktuatorData(text)
					text.contains("herbalawu/setpoint") -> parseAndSaveSetPointData(text)
					else -> Log.d("MqttRepositoryImpl", "Unknown message topic")
				}
			}


			override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
				_connectionStatus.value = ConnectionStatus.DISCONNECTED
				Log.e("MqttRepositoryImpl", "WebSocket connection failed", t)
				retryWebSocket()
			}

			override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
				_connectionStatus.value = ConnectionStatus.DISCONNECTED
				Log.d("MqttRepositoryImpl", "WebSocket connection closed")
				retryWebSocket()
			}
		})
	}



	private fun sendHeartbeat(): Flow<Unit> = flow{
		while (_connectionStatus.value == ConnectionStatus.CONNECTED) {
			delay(30000) // Send heartbeat setiap 30 detik
			webSocket?.send("ping")
			emit(Unit) // Emit untuk memberi tahu status
			Log.d("MqttRepositoryImpl", "Heartbeat sent: ping")
		}
	}

	@RequiresApi(Build.VERSION_CODES.O)
	private fun parseAndSaveMonitoringData(message: String) {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				val jsonObject = JSONObject(message)
				val topic = jsonObject.getString("topic")
				if (topic == "herbalawu/monitoring") {
					val dataObject = jsonObject.getJSONObject("data")
					val timestamp = jsonObject.optString("timestamp", Instant.now().toString())

					val data = MonitoringData(
						watertemp = dataObject.getDouble("watertemp").toFloat(),
						waterppm = dataObject.getDouble("waterppm").toFloat(),
						waterph = dataObject.getDouble("waterph").toFloat(),
						airtemp = dataObject.getDouble("airtemp").toFloat(),
						airhum = dataObject.getDouble("airhum").toFloat(),
						timestamp = timestamp
					)
					Log.d("MqttRepositoryImpl", "Parsed MonitoringData: $data")
					dataStoreManager.saveMonitoringData(data)
				}
			} catch (e: Exception) {
				Log.e("MqttRepositoryImpl", "Error parsing message", e)
			}
		}
	}


	@RequiresApi(Build.VERSION_CODES.O)
	private fun parseAndSaveAktuatorData(message: String) {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				val jsonObject = JSONObject(message)
				val topic = jsonObject.getString("topic")
				if (topic == "herbalawu/aktuator") {
					val dataObject = jsonObject.getJSONObject("data")
					val timestamp = jsonObject.optString("timestamp", Instant.now().toString())

					val data = AktuatorData(
						timestamp = timestamp,
						actuator_nutrisi = dataObject.optBoolean("actuator_nutrisi", false),
						actuator_ph_up = dataObject.optBoolean("actuator_ph_up", false),
						actuator_ph_down = dataObject.optBoolean("actuator_ph_down", false),
						actuator_air_baku = dataObject.optBoolean("actuator_air_baku", false),
						actuator_pompa_utama_1 = dataObject.optBoolean("actuator_pompa_utama_1", false),
						actuator_pompa_utama_2 = dataObject.optBoolean("actuator_pompa_utama_2", false)
					)
					Log.d("MqttRepositoryImpl", "Parsed AktuatorData: $data")
					dataStoreManager.saveAktuatorData(data)
				}
			} catch (e: Exception) {
				Log.e("MqttRepositoryImpl", "Error parsing aktuator message", e)
			}
		}
	}

	@RequiresApi(Build.VERSION_CODES.O)
	private fun parseAndSaveSetPointData(message: String) {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				val jsonObject = JSONObject(message)
				val topic = jsonObject.getString("topic")
				if (topic == "herbalawu/setpoint") {
					val dataObject = jsonObject.getJSONObject("data")
					val timestamp = jsonObject.optString("timestamp", Instant.now().toString())

					val data = SetPointData(
						timestamp = timestamp,
						watertemp = dataObject.getDouble("watertemp").toFloat(),
						waterppm = dataObject.getDouble("waterppm").toFloat(),
						waterph = dataObject.getDouble("waterph").toFloat(),
						profile = dataObject.getString("profile")
					)
					Log.d("MqttRepositoryImpl", "Parsed SetPointData: $data")
					dataStoreManager.saveSetPointData(data)
				}
			} catch (e: Exception) {
				Log.e("MqttRepositoryImpl", "Error parsing setpoint message", e)
			}
		}
	}

}