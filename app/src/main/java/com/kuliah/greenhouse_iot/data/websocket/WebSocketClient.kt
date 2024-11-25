package com.kuliah.greenhouse_iot.data.websocket

import com.kuliah.greenhouse_iot.util.Constants.WEBSOCKET_URL
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class WebSocketClient(
	private val webSocketUrl: String = WEBSOCKET_URL
) {

	private var webSocket: WebSocket? = null
	private val _webSocketDataFlow = MutableSharedFlow<JSONObject>()
	val webSocketDataFlow: SharedFlow<JSONObject> = _webSocketDataFlow

	private val client: OkHttpClient = OkHttpClient.Builder()
		.readTimeout(0, TimeUnit.MILLISECONDS)
		.build()

	fun startListening(onMessageReceived: (JSONObject) -> Unit) {
		val request = Request.Builder().url(webSocketUrl).build()
		webSocket = client.newWebSocket(request, object : WebSocketListener() {
			override fun onMessage(webSocket: WebSocket, text: String) {
				try {
					val json = JSONObject(text)
					_webSocketDataFlow.tryEmit(json)
					onMessageReceived(json)
				} catch (e: Exception) {
					e.printStackTrace()
				}
			}

			override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
				t.printStackTrace()
				reconnect()
			}

			override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
				reconnect()
			}
		})
	}

	private fun reconnect() {
		webSocket?.close(1000, null)
		webSocket = null

		GlobalScope.launch {
			delay(5000) // Tunggu 5 detik sebelum reconnect
			startListening {}
		}
	}

	fun stopListening() {
		webSocket?.close(1000, null)
		webSocket = null
	}
}
