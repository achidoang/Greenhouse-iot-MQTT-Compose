package com.kuliah.greenhouse_iot.util

import android.content.Context

object AssetLoader {
	fun loadHtml(fileName: String, context: Context): String {
		return context.assets.open(fileName).bufferedReader().use { it.readText() }
	}
}