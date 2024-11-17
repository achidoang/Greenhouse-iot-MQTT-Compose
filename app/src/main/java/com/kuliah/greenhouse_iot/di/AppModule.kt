package com.kuliah.greenhouse_iot.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.media3.common.util.UnstableApi
import com.kuliah.greenhouse_iot.data.local.datastore.DataStoreManager
import com.kuliah.greenhouse_iot.data.repository.HistoryRepositoryImpl
import com.kuliah.greenhouse_iot.data.repository.MqttRepositoryImpl
import com.kuliah.greenhouse_iot.domain.repository.HistoryRepository
import com.kuliah.greenhouse_iot.domain.repository.MqttRepository
import com.kuliah.greenhouse_iot.util.Constants.AUTH_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@OptIn(UnstableApi::class)
object AppModule {


	@Singleton
	@Provides
	fun provideDatastoreInstance(@ApplicationContext context: Context) =
		PreferenceDataStoreFactory.create {
			context.preferencesDataStoreFile("settings")
		}


	@Singleton
	@Provides
	fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

	@Singleton
	@Provides
	fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
		return DataStoreManager(context)
	}

	@Singleton
	@Provides
	fun provideMqttRepository(
		okHttpClient: OkHttpClient,
		dataStoreManager: DataStoreManager
	): MqttRepository = MqttRepositoryImpl(okHttpClient, dataStoreManager)

	@Provides
	@Singleton
	fun provideHistoryRepository(dataStoreManager: DataStoreManager): HistoryRepository {
		return HistoryRepositoryImpl(dataStoreManager)
	}

}

