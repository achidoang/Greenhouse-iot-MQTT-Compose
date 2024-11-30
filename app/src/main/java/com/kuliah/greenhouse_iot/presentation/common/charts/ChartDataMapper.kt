package com.kuliah.greenhouse_iot.presentation.common.charts

import com.github.mikephil.charting.data.Entry
import com.kuliah.greenhouse_iot.data.model.history.AverageHistory

object ChartDataMapper {
	fun toEntries(data: List<AverageHistory>, valueSelector: (AverageHistory) -> Float): List<Entry> {
		return data.mapIndexed { index, history ->
			Entry(index.toFloat(), valueSelector(history))
		}
	}
}