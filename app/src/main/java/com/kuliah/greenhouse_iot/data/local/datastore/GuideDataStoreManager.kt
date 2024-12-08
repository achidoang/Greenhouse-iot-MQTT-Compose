package com.kuliah.greenhouse_iot.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kuliah.greenhouse_iot.data.model.guide.Guide
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class GuideCache(val guides: List<Guide>)

object GuideCacheSerializer : Serializer<GuideCache> {
	override val defaultValue: GuideCache = GuideCache(emptyList())

	override suspend fun readFrom(input: InputStream): GuideCache {
		return Json.decodeFromString(input.readBytes().decodeToString())
	}

	override suspend fun writeTo(t: GuideCache, output: OutputStream) {
		output.write(Json.encodeToString(t).toByteArray())
	}
}

private val Context.dataStore: DataStore<GuideCache> by dataStore(
	fileName = "guide_cache.json",
	serializer = GuideCacheSerializer
)

@Singleton
class GuideDataStoreManager @Inject constructor(
	private val context: Context
) {

	val guidesFlow: Flow<List<Guide>> = context.dataStore.data
		.map { it.guides }
		.onEach { guides -> Log.d("GuideDataStoreManager", "Retrieved guides from DataStore: $guides") }


	suspend fun saveGuides(guides: List<Guide>) {
		Log.d("GuideDataStoreManager", "Saving guides: $guides")
		context.dataStore.updateData { GuideCache(guides) }
	}


}