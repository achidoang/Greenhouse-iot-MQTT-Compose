package com.kuliah.greenhouse_iot.presentation.screen.chart

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.kuliah.greenhouse_iot.data.model.history.AverageHistory
import com.kuliah.greenhouse_iot.presentation.common.charts.ChartDataMapper
import com.kuliah.greenhouse_iot.presentation.viewmodel.chart.ChartViewModel
import kotlinx.coroutines.launch
import java.lang.reflect.Array.set
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ChartScreen(viewModel: ChartViewModel = hiltViewModel()) {
	val pagerState = rememberPagerState()
	val tabs = listOf("Temp", "Water PPM", "Water PH", "Air Hum")
	val coroutineScope = rememberCoroutineScope()

	val backgroundColor = MaterialTheme.colorScheme.background
	val HeadColor = MaterialTheme.colorScheme.primary

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		TabRow(
			selectedTabIndex = pagerState.currentPage,
			modifier = Modifier.padding(bottom = 8.dp),
			containerColor = backgroundColor
		) {
			tabs.forEachIndexed { index, title ->
				Tab(
					selected = pagerState.currentPage == index,
					onClick = {
						coroutineScope.launch {
							pagerState.scrollToPage(index)
						}
					},
					text = { Text(text = title, color = HeadColor, style = MaterialTheme.typography.bodySmall) }

				)
			}
		}

		HorizontalPager(
			count = tabs.size,
			state = pagerState,
			modifier = Modifier.fillMaxSize()
		) { page ->
			Column(
				modifier = Modifier
					.fillMaxSize(),
				verticalArrangement = Arrangement.Top
			) {
				when (page) {
					0 -> {
						CombinedDailyChart(viewModel)
						CombinedWeeklyChart(viewModel)
					}

					1 -> {
						WaterPpmDailyChart(viewModel)
						WaterPpmWeeklyChart(viewModel)
					}

					2 -> {
						WaterPhDailyChart(viewModel)
						WaterPhWeeklyChart(viewModel)
					}

					3 -> {
						AirHumDailyChart(viewModel)
						AirHumWeeklyChart(viewModel)
					}
				}
			}
		}
	}
}

@Composable
fun CombinedDailyChart(viewModel: ChartViewModel) {

	ChartContent(
		data = viewModel.dailyAverages.collectAsState().value,
		metrics = listOf(
			"Water Temp (°C)" to { it.avg_watertemp },
			"Air Temp (°C)" to { it.avg_airtemp },
//			"Air Humidity" to { it.avg_airhum }
		),
		xAxisLabel = "Day",
		yAxisLabel = "Value",
		title = "Daily Average",
		labels = viewModel.dailyXAxisLabels.collectAsState().value
	)
}

@Composable
fun CombinedWeeklyChart(viewModel: ChartViewModel) {
	ChartContent(
		data = viewModel.weeklyAverages.collectAsState().value,
		metrics = listOf(
			"Water Temp (°C)" to { it.avg_watertemp },
			"Air Temp (°C)" to { it.avg_airtemp },
//			"Air Humidity" to { it.avg_airhum }
		),
		xAxisLabel = "Week",
		yAxisLabel = "Value",
		title = "Weekly Average",
		labels = viewModel.weeklyXAxisLabels.collectAsState().value
	)
}

@Composable
fun AirHumDailyChart(viewModel: ChartViewModel) {
	ChartContent(
		data = viewModel.dailyAverages.collectAsState().value,
		metrics = listOf(
			"Air Hum (%)" to { it.avg_airhum }
		),
		xAxisLabel = "Day",
		yAxisLabel = "Hum",
		title = "Daily Humidity",
		labels = viewModel.dailyXAxisLabels.collectAsState().value
	)
}

@Composable
fun AirHumWeeklyChart(viewModel: ChartViewModel) {
	ChartContent(
		data = viewModel.weeklyAverages.collectAsState().value,
		metrics = listOf(
			"Air Hum (%)" to { it.avg_airhum }
		),
		xAxisLabel = "Week",
		yAxisLabel = "Hum",
		title = "Weekly Humidity",
		labels = viewModel.weeklyXAxisLabels.collectAsState().value
	)
}

@Composable
fun WaterPpmDailyChart(viewModel: ChartViewModel) {
	ChartContent(
		data = viewModel.dailyAverages.collectAsState().value,
		metrics = listOf(
			"Water PPM" to { it.avg_waterppm }
		),
		xAxisLabel = "Day",
		yAxisLabel = "PPM",
		title = "Daily Nutrisi",
		labels = viewModel.dailyXAxisLabels.collectAsState().value
	)
}

@Composable
fun WaterPpmWeeklyChart(viewModel: ChartViewModel) {
	ChartContent(
		data = viewModel.weeklyAverages.collectAsState().value,
		metrics = listOf(
			"Water PPM" to { it.avg_waterppm }
		),
		xAxisLabel = "Week",
		yAxisLabel = "PPM",
		title = "Weekly Nutrisi",
		labels = viewModel.weeklyXAxisLabels.collectAsState().value
	)
}

@Composable
fun WaterPhDailyChart(viewModel: ChartViewModel) {
	ChartContent(
		data = viewModel.dailyAverages.collectAsState().value,
		metrics = listOf(
			"Water PH" to { it.avg_waterph }
		),
		xAxisLabel = "Day",
		yAxisLabel = "pH",
		title = "Daily pH Air",
		labels = viewModel.dailyXAxisLabels.collectAsState().value
	)
}

@Composable
fun WaterPhWeeklyChart(viewModel: ChartViewModel) {
	ChartContent(
		data = viewModel.weeklyAverages.collectAsState().value,
		metrics = listOf(
			"Water PH" to { it.avg_waterph }
		),
		xAxisLabel = "Week",
		yAxisLabel = "pH",
		title = "Weekly pH Air",
		labels = viewModel.weeklyXAxisLabels.collectAsState().value
	)
}

@Composable
fun ChartContent(
	data: List<AverageHistory>?,
	metrics: List<Pair<String, (AverageHistory) -> Float>>,
	xAxisLabel: String,
	yAxisLabel: String,
	title: String,
	//	isWeekly: Boolean,
	labels: List<String>
) {
	val headColor = MaterialTheme.colorScheme.onSurface

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		if (data.isNullOrEmpty()) {
			Text(
				text = "Data tidak tersedia",
				style = MaterialTheme.typography.bodyMedium,
				color = headColor,
			)
		} else {
			Text(
				text = title,
				style = MaterialTheme.typography.headlineSmall,
				color = headColor,
				modifier = Modifier.padding(bottom = 8.dp)
			)
			Card(
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 30.dp)
					.padding(8.dp),
				shape = MaterialTheme.shapes.medium,
				elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
			) {
				CombinedLineChartComposable(
					modifier = Modifier
						.fillMaxWidth()
						.height(230.dp),
					data = data,
					metrics = metrics,
					xAxisLabel = xAxisLabel,
					yAxisLabel = yAxisLabel,
					labels = labels,
					isDarkTheme = isSystemInDarkTheme()
				)
			}
		}
	}
}


@Composable
fun CombinedLineChartComposable(
	modifier: Modifier = Modifier,
	data: List<AverageHistory>,
	metrics: List<Pair<String, (AverageHistory) -> Float>>,
	xAxisLabel: String,
	yAxisLabel: String,
	labels: List<String>,
	isDarkTheme: Boolean
) {
	val context = LocalContext.current
	AndroidView(
		modifier = modifier,
		factory = {
			createCombinedLineChart(
				context,
				data,
				metrics,
				xAxisLabel,
				yAxisLabel,
				isDarkTheme,
				labels
			)
		},
		update = { chart ->
			updateCombinedChartWithNewData(
				chart,
				data,
				metrics,
				xAxisLabel,
				yAxisLabel,
				isDarkTheme,
				labels
			)
		}
	)
}


private fun createCombinedLineChart(
	context: Context,
	data: List<AverageHistory>,
	metrics: List<Pair<String, (AverageHistory) -> Float>>,
	xAxisLabel: String,
	yAxisLabel: String,
	isDarkTheme: Boolean,
	labels: List<String>
): LineChart {
	val chart = LineChart(context)

	var textColor = if (isDarkTheme) Color.WHITE else Color.BLACK
	val backgroundColor = if (isDarkTheme) Color.DKGRAY else Color.WHITE

	// Updated colors to match the lines in the chart
	val colors = listOf(
		Color.rgb(0, 160, 160),  // Cyan for Water Temp
		Color.rgb(0, 0, 255),    // Blue for Air Temp
		Color.rgb(0, 255, 0)     // Green for Air Humidity
	)

	val dataSets = metrics.mapIndexed { index, (label, selector) ->
		LineDataSet(data.mapIndexed { dataIndex, history ->
			Entry(dataIndex.toFloat(), selector(history))
		}, label).apply {
			color = colors[index]
			valueTextColor = textColor
			lineWidth = 2f
			setDrawCircles(true)
			setDrawCircleHole(true)
			circleRadius = 4f
			circleHoleRadius = 2f
			setDrawValues(false)
			mode = LineDataSet.Mode.CUBIC_BEZIER

			// Set circle colors to match line colors
			setCircleColor(colors[index])
			circleHoleColor = backgroundColor
		}
	}

	chart.apply {
		this.data = LineData(dataSets)
		description.isEnabled = false
		legend.apply {
			textColor = textColor
			textSize = 12f
			isEnabled = true
			verticalAlignment = Legend.LegendVerticalAlignment.TOP
			horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
			orientation = Legend.LegendOrientation.HORIZONTAL
			setDrawInside(false)
			yOffset = 15f
			xOffset = 15f
			form = Legend.LegendForm.CIRCLE
			formSize = 10f
			formLineWidth = 2f
		}
		setBackgroundColor(backgroundColor)

		xAxis.apply {
			position = XAxis.XAxisPosition.BOTTOM
			setDrawGridLines(false)
			granularity = 1f
			labelRotationAngle = 0f
			textColor = textColor
			valueFormatter = createXAxisFormatter(labels)
		}

		axisLeft.apply {
			textColor = textColor
			setDrawGridLines(true)
			gridColor = Color.LTGRAY
		}

		axisRight.isEnabled = false

		setTouchEnabled(true)
		isDragEnabled = true
		setScaleEnabled(true)
		setPinchZoom(true)

		animateX(1000)
	}

	return chart
}

private fun updateCombinedChartWithNewData(
	chart: LineChart,
	data: List<AverageHistory>,
	metrics: List<Pair<String, (AverageHistory) -> Float>>,
	xAxisLabel: String,
	yAxisLabel: String,
	isDarkTheme: Boolean,
	labels: List<String>
) {
	var textColor = if (isDarkTheme) Color.WHITE else Color.BLACK
	val backgroundColor = if (isDarkTheme) Color.GRAY else Color.WHITE

	// Updated colors to match the lines in the chart
	val colors = listOf(
		Color.rgb(0, 160, 160),  // Cyan for Water Temp
		Color.rgb(110, 0, 255),    // Ungu for Air Temp
		Color.rgb(255, 230, 0)     // Yellow for Air Humidity
	)

	val dataSets = metrics.mapIndexed { index, (label, selector) ->
		LineDataSet(data.mapIndexed { dataIndex, history ->
			Entry(dataIndex.toFloat(), selector(history))
		}, label).apply {
			color = colors[index]
			valueTextColor = textColor
			lineWidth = 2f
			setDrawCircles(true)
			setDrawCircleHole(true)
			circleRadius = 4f
			circleHoleRadius = 2f
			setDrawValues(false)
			mode = LineDataSet.Mode.CUBIC_BEZIER

			// Set circle colors to match line colors
			setCircleColor(colors[index])
			circleHoleColor = backgroundColor
		}
	}

	chart.apply {
		this.data = LineData(dataSets)
		setBackgroundColor(backgroundColor)
		xAxis.apply {
			textColor = textColor
			valueFormatter = createXAxisFormatter(labels)
		}
		axisLeft.textColor = textColor
		legend.textColor = textColor
		invalidate()
	}
}



//private fun createXAxisFormatter(labels: List<String>): ValueFormatter {
//	return object : ValueFormatter() {
//		override fun getFormattedValue(value: Float): String {
//			val index = value.toInt()
//			return if (index in labels.indices) labels[index] else ""
//		}
//	}
//}


private fun createXAxisFormatter(isWeekly: Boolean): ValueFormatter {
	return object : ValueFormatter() {
		override fun getFormattedValue(value: Float): String {
			return if (isWeekly) {
				"Minggu ${(value + 1).toInt()}"
			} else {
				// Menggunakan format hari dalam bahasa Indonesia
				SimpleDateFormat("EEEE", Locale("id")).format(Date(value.toLong() * 86400000))
			}
		}
	}
}

private fun createXAxisFormatter(labels: List<String>): ValueFormatter {
	return object : ValueFormatter() {
		override fun getFormattedValue(value: Float): String {
			val index = value.toInt()
			return if (index in labels.indices) labels[index] else ""
		}
	}
}

