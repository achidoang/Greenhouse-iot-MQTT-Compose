package com.kuliah.greenhouse_iot.presentation.screen.chart

import android.os.Build
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.google.gson.Gson
import com.kuliah.greenhouse_iot.R
import com.kuliah.greenhouse_iot.data.model.history.AverageHistory
import com.kuliah.greenhouse_iot.presentation.common.LottieLoading
import com.kuliah.greenhouse_iot.presentation.viewmodel.chart.ChartViewModel

@Composable
fun ChartScreen(viewModel: ChartViewModel = hiltViewModel()) {
	val dailyAverages by viewModel.dailyAverages.collectAsState()
	val weeklyAverages by viewModel.weeklyAverages.collectAsState()
	val isLoading by viewModel.isLoading.collectAsState()
	val error by viewModel.error.collectAsState()

	val tabs = listOf("Temp & Hum", "Water PPM", "Water PH")
	var selectedTabIndex by remember { mutableStateOf(0) }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	) {
		// Custom TabRow
		TabRow(
			selectedTabIndex = selectedTabIndex,
			containerColor = MaterialTheme.colorScheme.surface,
			contentColor = MaterialTheme.colorScheme.onSurface,
			indicator = { tabPositions ->
				TabRowDefaults.Indicator(
					Modifier
						.tabIndicatorOffset(tabPositions[selectedTabIndex])
						.height(3.dp)
						.background(MaterialTheme.colorScheme.primary)
				)
			}
		) {
			tabs.forEachIndexed { index, title ->
				Tab(
					selected = selectedTabIndex == index,
					onClick = { selectedTabIndex = index },
					text = { Text(title) }
				)
			}
		}

		// Content area
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp)
		) {
			when {
				isLoading -> {
					Box(
						modifier = Modifier
							.fillMaxSize(),
						contentAlignment = Alignment.Center
					) {
						LottieLoading()
					}
				}
				error != null -> {
					Text(
						text = error ?: "An unknown error occurred",
						color = MaterialTheme.colorScheme.error,
						modifier = Modifier.align(Alignment.Center)
					)
				}
				else -> {
					dailyAverages?.let {
						weeklyAverages?.let { it1 ->
							ChartContent(
								dailyAverages = it,
								weeklyAverages = it1,
								selectedTabIndex = selectedTabIndex
							)
						}
					}
				}
			}
		}
	}
}

@Composable
fun ChartContent(
	dailyAverages: List<AverageHistory>,
	weeklyAverages: List<AverageHistory>,
	selectedTabIndex: Int
) {
	val category = when (selectedTabIndex) {
		0 -> "temp_humidity"
		1 -> "water_ppm"
		2 -> "water_ph"
		else -> "temp_humidity"
	}

	Column(
		modifier = Modifier.fillMaxSize()
	) {
		// Daily Chart Section
		Text(
			"Daily Chart",
			style = MaterialTheme.typography.titleLarge.copy(
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.onSurface
			),
			modifier = Modifier.padding(bottom = 8.dp)
		)
		ChartCard {
			ChartWebView(
				data = dailyAverages,
				category = category,
				isWeekly = false
			)
		}

		Spacer(modifier = Modifier.height(24.dp))

		// Weekly Chart Section
		Text(
			"Weekly Chart",
			style = MaterialTheme.typography.titleLarge.copy(
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.onSurface
			),
			modifier = Modifier.padding(bottom = 8.dp)
		)
		ChartCard {
			ChartWebView(
				data = weeklyAverages,
				category = category,
				isWeekly = true
			)
		}

		Spacer(modifier = Modifier.height(80.dp))
	}
}

@Composable
fun ChartCard(content: @Composable () -> Unit) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.height(240.dp)
			.padding(vertical = 8.dp),
		shape = MaterialTheme.shapes.medium,
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(8.dp)
		) {
			content()
		}
	}
}

@Composable
fun ChartWebView(data: List<AverageHistory>, category: String, isWeekly: Boolean) {
	val context = LocalContext.current
	val webView = remember { WebView(context).apply { settings.javaScriptEnabled = true } }

	LaunchedEffect(data, category, isWeekly) {
		val labels = data.map {
			if (isWeekly) {
				"Week ${it.week}"
			} else {
				it.day?.toString() ?: ""
			}
		}

		// Determine chartData and yMin and yMax limits for each category
		val (chartData, yMin, yMax) = when (category) {
			"temp_humidity" -> Triple(
				mapOf(
					"Water Temp" to data.map { it.avg_watertemp },
					"Air Temp" to data.map { it.avg_airtemp },
					"Air Humidity" to data.map { it.avg_airhum }
				),
				0,  // Minimum value for temp_humidity
				100  // Maximum value for temp_humidity
			)
			"water_ppm" -> Triple(
				mapOf("Water PPM" to data.map { it.avg_waterppm }),
				0,   // Minimum value for water_ppm
				1000  // Maximum value for water_ppm
			)
			"water_ph" -> Triple(
				mapOf("Water PH" to data.map { it.avg_waterph }),
				0,   // Minimum value for water_ph
				14    // Maximum value for water_ph
			)
			else -> Triple(emptyMap(), 0, 100)
		}

		val jsonLabels = Gson().toJson(labels)
		val jsonDatasets = Gson().toJson(
			chartData.entries.mapIndexed { index, (label, values) ->
				mapOf(
					"label" to label,
					"data" to values,
					"borderColor" to listOf("cyan", "blue", "orange", "red")[index % 4],
					"backgroundColor" to "rgba(54, 162, 235, 0.2)"
				)
			}
		)

		// Send yMin and yMax values to JavaScript function
		webView.evaluateJavascript(
			"updateChart('line', $jsonLabels, $jsonDatasets, $yMin, $yMax)"
		) {}
	}

	AndroidView(
		factory = { webView.apply { loadUrl("file:///android_asset/chart.html") } },
		modifier = Modifier
			.fillMaxWidth()
			.height(240.dp)
	)
}
