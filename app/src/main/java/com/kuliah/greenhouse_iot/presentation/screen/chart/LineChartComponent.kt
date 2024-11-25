//package com.kuliah.greenhouse_iot.presentation.screen.chart
//
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.viewinterop.AndroidView
//import com.kuliah.greenhouse_iot.data.model.history.AverageHistory
//
//@Composable
//fun LineChartComponent(data: List<AverageHistory>) {
//	// Contoh implementasi
//	val lineData = data.mapIndexed { index, average ->
//		index.toFloat() to average.avg_watertemp // Ganti dengan parameter lain jika perlu
//	}
//
//	LineChart(
//		data = lineData,
//		modifier = Modifier
//			.fillMaxWidth()
//			.height(300.dp),
//		config = LineChartConfig(
//			xAxisGridLines = true,
//			yAxisGridLines = true,
//			touchEnabled = true,
//			tooltipEnabled = true,
//			zoomEnabled = true
//		)
//	)
//}
