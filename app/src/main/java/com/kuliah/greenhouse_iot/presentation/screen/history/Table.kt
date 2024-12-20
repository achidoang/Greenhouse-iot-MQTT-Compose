package com.kuliah.greenhouse_iot.presentation.screen.history

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuliah.greenhouse_iot.data.model.history.MonitoringHistory

@Composable
fun HistoryTable(
	history: List<MonitoringHistory>,
	headColor: Color,
	textColor: Color,
	bgColor: Color
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(containerColor = bgColor),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		val scrollState = rememberScrollState()
		Column {
			Row(
				modifier = Modifier
					.horizontalScroll(scrollState)
					.background(headColor.copy(alpha = 0.1f))
					.padding(vertical = 15.dp)

			) {
				HistoryTableHeader(headColor)
			}
			LazyColumn {
				items(history) { item ->
					Row(
						modifier = Modifier
							.horizontalScroll(scrollState)
							.padding(vertical = 12.dp)
					) {
						HistoryTableRow(item, textColor)
					}
					Divider(color = textColor.copy(alpha = 0.1f))
				}
			}
		}
	}
}

@Composable
fun HistoryTableHeader(headColor: Color) {
	TableCell(text = "Timestamp", width = 180.dp, color = headColor, bold = true)
	TableCell(text = "Water Temp", width = 120.dp, color = headColor, bold = true)
	TableCell(text = "Water PPM", width = 120.dp, color = headColor, bold = true)
	TableCell(text = "Water pH", width = 120.dp, color = headColor, bold = true)
	TableCell(text = "Air Temp", width = 120.dp, color = headColor, bold = true)
	TableCell(text = "Air Humidity", width = 120.dp, color = headColor, bold = true)
}

@Composable
fun HistoryTableRow(item: MonitoringHistory, textColor: Color) {
	val formattedTimestamp = item.timestamp.formatToReadableDate()
	TableCell(text = formattedTimestamp, width = 180.dp, color = textColor)
	TableCell(text = "%.1f°C".format(item.watertemp), width = 120.dp, color = textColor)
	TableCell(text = "%.1f".format(item.waterppm), width = 120.dp, color = textColor)
	TableCell(text = "%.1f".format(item.waterph), width = 120.dp, color = textColor)
	TableCell(text = "%.1f°C".format(item.airtemp), width = 120.dp, color = textColor)
	TableCell(text = "%.1f%%".format(item.airhum), width = 120.dp, color = textColor)
}

@Composable
fun TableCell(text: String, width: Dp, color: Color, bold: Boolean = false) {
	Text(
		text = text,
		modifier = Modifier
			.width(width)
			.padding(horizontal = 8.dp),
		style = MaterialTheme.typography.bodyMedium.copy(
			fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
		),
		color = color
	)
}

