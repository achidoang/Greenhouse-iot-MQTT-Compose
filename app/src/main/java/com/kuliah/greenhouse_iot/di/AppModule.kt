package com.kuliah.greenhouse_iot.di

import android.app.Application
import android.content.Context
import androidx.annotation.OptIn
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kuliah.greenhouse_iot.data.remote.ApiService
import com.kuliah.greenhouse_iot.data.remote.mqtt.MqttClientService
import com.kuliah.greenhouse_iot.data.repository.MonitoringRepositoryImpl
import com.kuliah.greenhouse_iot.data.repository.UserRepositoryImpl
import com.kuliah.greenhouse_iot.domain.repository.MonitoringRepository
import com.kuliah.greenhouse_iot.domain.repository.UserRepository
import com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt.SubscribeMqttUseCase
import com.kuliah.greenhouse_iot.util.Constants.AUTH_URL
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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

	@Provides
	@Singleton
	fun provideMqttClientService(
		@ApplicationContext context: Context
	): MqttClientService {
		return MqttClientService(context)
	}

	@Provides
	@Singleton
	fun provideMonitoringRepository(
		mqttClientService: MqttClientService
	): MonitoringRepository {
		return MonitoringRepositoryImpl(mqttClientService)
	}

	@Provides
	@Singleton
	fun provideRetrofit(): Retrofit {
		return Retrofit.Builder()
			.baseUrl(AUTH_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	@Provides
	@Singleton
	fun provideApiService(retrofit: Retrofit): ApiService {
		return retrofit.create(ApiService::class.java)
	}

	@Provides
	@Singleton
	fun provideUserRepository(apiService: ApiService): UserRepository {
		return UserRepositoryImpl(apiService)
	}

}

