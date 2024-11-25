package com.kuliah.greenhouse_iot.presentation.screen.chart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.data.model.history.AverageHistory
import com.kuliah.greenhouse_iot.presentation.common.AnimatedLoading
import com.kuliah.greenhouse_iot.presentation.common.LottieLoading
import com.kuliah.greenhouse_iot.presentation.viewmodel.chart.AverageHistoryViewModel
import com.madrapps.plot.line.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AverageHistoryScreen(
	viewModel: AverageHistoryViewModel = hiltViewModel(),
) {
	val dailyData by viewModel.dailyData.collectAsState()
	val weeklyData by viewModel.weeklyData.collectAsState()
	val monthlyData by viewModel.monthlyData.collectAsState()
	val isLoading by viewModel.isLoading.collectAsState()

	var selectedTab by remember { mutableStateOf(0) } // 0 = Daily, 1 = Weekly, 2 = Monthly

	// Mengambil data sesuai tab yang dipilih
	LaunchedEffect(selectedTab) {
		when (selectedTab) {
			0 -> viewModel.loadDailyData()
			1 -> viewModel.loadWeeklyData()
			2 -> viewModel.loadMonthlyData()
		}
	}

	Scaffold(
		topBar = { TopAppBar(title = { Text("Average History") }) }
	) { padding ->
		Column(modifier = Modifier.padding(padding)) {
			TabRow(selectedTabIndex = selectedTab) {
				Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) { Text("Daily") }
				Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) { Text("Weekly") }
				Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) { Text("Monthly") }
			}

			if (isLoading) {
				Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
					LottieLoading()
				}
			} else {
				// Pilih data yang sesuai berdasarkan tab yang dipilih
				val data = when (selectedTab) {
					0 -> dailyData
					1 -> weeklyData
					else -> monthlyData
				}
				AverageChart(data = data)
			}
		}
	}
}

@Composable
fun AverageChart(data: List<AverageHistory>) {
	// Memastikan data tidak kosong sebelum mencoba menggambar grafik
	if (data.isEmpty()) {
		Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			Text("No data available", style = MaterialTheme.typography.headlineSmall)
		}
		return
	}

	// Mengonversi data menjadi DataPoint untuk digunakan dalam grafik
	val dataPoints = data.mapIndexed { index, averageHistory ->
		// Pastikan data day tidak null dan memiliki nilai yang valid
		val xValue = averageHistory.day?.toFloat() ?: index.toFloat() // Jika day null, gunakan index sebagai pengganti
		DataPoint(
			x = xValue,
			y = averageHistory.avg_watertemp ?: 0f // Pastikan avg_watertemp valid, gunakan 0 jika null
		)
	}

	// Menampilkan grafik menggunakan LineGraph
	LineGraph(
		plot = LinePlot(
			lines = listOf(
				LinePlot.Line(
					connection = LinePlot.Connection(color = Color.Blue),
					intersection = LinePlot.Intersection(color = Color.Red),
					highlight = LinePlot.Highlight(color = Color.Yellow),
					dataPoints = dataPoints,
				)
			),
			grid = LinePlot.Grid(Color.Gray, steps = 5),
		),
		modifier = Modifier
			.fillMaxWidth()
			.height(300.dp),
		onSelection = { xLine, points ->
			// Tangani interaksi jika diperlukan (misalnya menampilkan tooltip)
		}
	)
}
