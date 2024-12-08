package com.kuliah.greenhouse_iot.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.media3.common.util.UnstableApi
import androidx.room.Room
import androidx.work.WorkManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.kuliah.greenhouse_iot.data.local.datastore.AuthDataStoreManager
import com.kuliah.greenhouse_iot.data.local.datastore.DataStoreManager
import com.kuliah.greenhouse_iot.data.local.datastore.GuideDataStoreManager
import com.kuliah.greenhouse_iot.data.model.auth.AuthInterceptor
import com.kuliah.greenhouse_iot.data.remote.api.akun.UserApi
import com.kuliah.greenhouse_iot.data.remote.api.auth.AuthApi
import com.kuliah.greenhouse_iot.data.remote.api.control.AktuatorApi
import com.kuliah.greenhouse_iot.data.remote.api.guide.GuideApi
import com.kuliah.greenhouse_iot.data.remote.api.history.MonitoringApi
import com.kuliah.greenhouse_iot.data.remote.api.mode.ModeApi
import com.kuliah.greenhouse_iot.data.remote.api.profile.ProfileApi
import com.kuliah.greenhouse_iot.data.remote.api.weather.WeatherApi
import com.kuliah.greenhouse_iot.data.repository.AktuatorRepositoryImpl
import com.kuliah.greenhouse_iot.data.repository.AuthRepositoryImpl
import com.kuliah.greenhouse_iot.data.repository.GuideRepositoryImpl
import com.kuliah.greenhouse_iot.data.repository.HistoryRepositoryImpl
import com.kuliah.greenhouse_iot.data.repository.ModeRepositoryImpl
import com.kuliah.greenhouse_iot.data.repository.MonitoringRepositoryImpl
import com.kuliah.greenhouse_iot.data.repository.MqttRepositoryImpl
import com.kuliah.greenhouse_iot.data.repository.ProfileRepositoryImpl
import com.kuliah.greenhouse_iot.data.repository.UserRepositoryImpl
import com.kuliah.greenhouse_iot.data.repository.WeatherRepositoryImpl
import com.kuliah.greenhouse_iot.data.websocket.WebSocketClient
import com.kuliah.greenhouse_iot.domain.repository.AktuatorRepository
import com.kuliah.greenhouse_iot.domain.repository.AuthRepository
import com.kuliah.greenhouse_iot.domain.repository.GuideRepository
import com.kuliah.greenhouse_iot.domain.repository.HistoryRepository
import com.kuliah.greenhouse_iot.domain.repository.ModeRepository
import com.kuliah.greenhouse_iot.domain.repository.MonitoringRepository
import com.kuliah.greenhouse_iot.domain.repository.MqttRepository
import com.kuliah.greenhouse_iot.domain.repository.ProfileRepository
import com.kuliah.greenhouse_iot.domain.repository.UserRepository
import com.kuliah.greenhouse_iot.domain.repository.WeatherRepository
import com.kuliah.greenhouse_iot.util.Constants.BASE_API_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@OptIn(UnstableApi::class)
object AppModule {

	@Provides
	@Singleton
	fun provideContext(@ApplicationContext context: Context): Context {
		return context
	}

	@Provides
	@Singleton
	fun provideAuthInterceptor(authDataStoreManager: AuthDataStoreManager): AuthInterceptor {
		return AuthInterceptor(authDataStoreManager)
	}

	@Singleton
	@Provides
	fun provideDatastoreInstance(@ApplicationContext context: Context) =
		PreferenceDataStoreFactory.create {
			context.preferencesDataStoreFile("settings")
		}

	@Singleton
	@Provides
	fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
		return DataStoreManager(context)
	}

	@Singleton
	@Provides
	fun provideAuthDataStoreManager(@ApplicationContext context: Context): AuthDataStoreManager {
		return AuthDataStoreManager(context)
	}

	@Provides
	@Singleton
	fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
		val logging = HttpLoggingInterceptor()
		logging.setLevel(HttpLoggingInterceptor.Level.BODY)
		return OkHttpClient.Builder()
			.addInterceptor(authInterceptor)
			.build()
	}

	@Provides
	@Singleton
	fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.baseUrl(BASE_API_URL)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	@Singleton
	@Provides
	fun provideAuthApi(retrofit: Retrofit): AuthApi {
		return retrofit.create(AuthApi::class.java)
	}

	@Singleton
	@Provides
	fun provideUserApi(retrofit: Retrofit): UserApi {
		return retrofit.create(UserApi::class.java)
	}

	@Provides
	@Singleton
	fun provideProfileApi(retrofit: Retrofit): ProfileApi {
		return retrofit.create(ProfileApi::class.java)
	}

	@Provides
	@Singleton
	fun provideWebSocketClient(): WebSocketClient {
		return WebSocketClient()
	}

	@Singleton
	@Provides
	fun provideMonitoringApi(retrofit: Retrofit): MonitoringApi {
		return retrofit.create(MonitoringApi::class.java)
	}


	@Provides
	@Singleton
	fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
		return retrofit.create(WeatherApi::class.java)
	}

	@Provides
	@Singleton
	fun provideModeApi(retrofit: Retrofit): ModeApi {
		return retrofit.create(ModeApi::class.java)
	}

	@Singleton
	@Provides
	fun provideAuthRepository(authApi: AuthApi): AuthRepository {
		return AuthRepositoryImpl(authApi)
	}

	@Provides
	@Singleton
	fun provideProfileRepository(
		profileApi: ProfileApi,
		webSocketClient: WebSocketClient
	): ProfileRepository {
		return ProfileRepositoryImpl(
			profileApi,
			webSocketClient
		)
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


	@Singleton
	@Provides
	fun provideUserRepository(api: UserApi): UserRepository {
		return UserRepositoryImpl(api)
	}

	@Provides
	@Singleton
	fun provideMonitoringRepository(api: MonitoringApi): MonitoringRepository {
		return MonitoringRepositoryImpl(api)
	}

	@Provides
	@Singleton
	fun provideWeatherRepository(api: WeatherApi): WeatherRepository {
		return WeatherRepositoryImpl(api)
	}

	@Provides
	@Singleton
	fun provideModeRepository(modeApi: ModeApi): ModeRepository {
		return ModeRepositoryImpl(modeApi)
	}


	@Provides
	@Singleton
	fun provideAktuatorApi(retrofit: Retrofit): AktuatorApi {
		return retrofit.create(AktuatorApi::class.java)
	}

	@Provides
	@Singleton
	fun provideAktuatorRepository(api: AktuatorApi): AktuatorRepository {
		return AktuatorRepositoryImpl(api)
	}

	@Provides
	@Singleton
	fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
		return LocationServices.getFusedLocationProviderClient(context)
	}

	@Provides
	@Singleton
	fun provideGuideApi(retrofit: Retrofit): GuideApi {
		return retrofit.create(GuideApi::class.java)
	}

	@Provides
	@Singleton
	fun provideGuideRepository(
		api: GuideApi,
		dataStoreManager: GuideDataStoreManager
	): GuideRepository {
		return GuideRepositoryImpl(api, dataStoreManager)
	}


	@Provides
	@Singleton
	fun provideGson(): Gson {
		return Gson()
	}

	@Provides
	@Singleton
	fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
		return WorkManager.getInstance(context)
	}

}



