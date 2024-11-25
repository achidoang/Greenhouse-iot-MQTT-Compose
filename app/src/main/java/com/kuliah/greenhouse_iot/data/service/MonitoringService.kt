package com.kuliah.greenhouse_iot.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kuliah.greenhouse_iot.R
import com.kuliah.greenhouse_iot.data.local.datastore.DataStoreManager
import com.kuliah.greenhouse_iot.data.repository.MqttRepositoryImpl
import com.kuliah.greenhouse_iot.util.ConnectionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class MonitoringService : Service() {

	private val notificationId = 1
	private val channelId = "MonitoringChannel"
	private var isRunning = false

	private val _serviceStatus = MutableStateFlow("Initializing...")
	val serviceStatus: StateFlow<String> = _serviceStatus


	override fun onCreate() {
		super.onCreate()
		createNotificationChannel()
		startForeground(notificationId, buildNotification("Initializing..."))
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		isRunning = true
		Log.d("MonitoringService", "Service started") // Tambahkan log
		CoroutineScope(Dispatchers.IO).launch {
			monitorConnection()
		}
		return START_STICKY
	}

	override fun onDestroy() {
		isRunning = false
		Log.d("MonitoringService", "Service stopped") // Tambahkan log
		super.onDestroy()
	}


	override fun onBind(intent: Intent?): IBinder? = null

	private fun createNotificationChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				channelId,
				"Monitoring Service",
				NotificationManager.IMPORTANCE_LOW
			)
			val manager = getSystemService(NotificationManager::class.java)
			manager.createNotificationChannel(channel)
		}
	}

	private fun buildNotification(content: String): Notification {
		return NotificationCompat.Builder(this, channelId)
			.setContentTitle("Device Monitoring")
			.setContentText(content)
			.setSmallIcon(R.drawable.ic_notification) // Ganti dengan ikon Anda
			.setOngoing(true)
			.build()
	}

	private suspend fun monitorConnection() {
		val repository = MqttRepositoryImpl(OkHttpClient(), DataStoreManager(applicationContext))

		val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

		repository.getConnectionStatus().collect { status ->
			val isConnectedToInternet = connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true
			val notificationContent = when {
				!isConnectedToInternet -> {
					_serviceStatus.value = "No Internet"
					"No internet connection"
				}
				status == ConnectionStatus.CONNECTED -> {
					_serviceStatus.value = "Online"
					"Device connected"
				}
				status == ConnectionStatus.DEVICE_OFFLINE -> {
					_serviceStatus.value = "Offline"
					"Device offline"
				}
				else -> {
					_serviceStatus.value = "Unknown"
					"Unknown status"
				}
			}

			Log.d("MonitoringService", "Connection status: $notificationContent")
			val notification = buildNotification(notificationContent)
			val manager = getSystemService(NotificationManager::class.java)
			manager.notify(notificationId, notification)
		}
	}


}
