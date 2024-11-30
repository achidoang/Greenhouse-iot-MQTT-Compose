plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	kotlin("kapt")
	id("com.google.dagger.hilt.android")

}

android {
	namespace = "com.kuliah.greenhouse_iot"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.kuliah.greenhouse_iot"
		minSdk = 24
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlinOptions {
		jvmTarget = "17"
	}
	buildFeatures {
		compose = true
	}
	kapt {
		correctErrorTypes = true
	}
	packaging {
		resources {
			excludes += "META-INF/INDEX.LIST"
			excludes += "META-INF/io.netty.versions.properties"
		}
	}
}

dependencies {
	implementation(libs.core.ktx)
	implementation(libs.androidx.runtime.livedata)
	val hilt = "2.48.1"
	val composeNavigation = "2.7.7"
	val hiltCompose = "1.1.0"
	val coroutines = "1.7.3"
	val room = "2.6.1"

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)

	//dagger-hilt
	implementation("com.google.dagger:hilt-android:${hilt}")
	implementation("androidx.hilt:hilt-navigation-compose:${hiltCompose}")
	kapt("com.google.dagger:hilt-android-compiler:${hilt}")

	//view model
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
//	implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
//	implementation("androidx.hilt:hilt-work:1.0.0")
//	kapt("androidx.hilt:hilt-compiler:1.0.0")

	//navigation
	implementation("androidx.navigation:navigation-compose:${composeNavigation}")

	//retrofit
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.okhttp3:okhttp:4.12.0")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation("com.squareup.moshi:moshi-kotlin:1.13.0")
	implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
	implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

	//coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutines}")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${coroutines}")

	//splashScreen
	implementation("androidx.core:core-splashscreen:1.1.0-alpha02")

	//coil
	implementation("io.coil-kt:coil-compose:2.4.0")

	//paging 3
	implementation("androidx.paging:paging-runtime-ktx:3.2.1")
	implementation("androidx.paging:paging-compose:3.3.0-alpha03")

	//room
	implementation("androidx.room:room-runtime:$room")
	implementation("androidx.room:room-ktx:$room")
	//noinspection KaptUsageInsteadOfKsp
	kapt("androidx.room:room-compiler:$room")
	implementation("androidx.room:room-paging:$room")

	//dataStore
	implementation("androidx.datastore:datastore-preferences:1.0.0")

	//more material icons
	implementation("androidx.compose.material:material-icons-extended")

	implementation("androidx.media3:media3-exoplayer:1.2.1")
	implementation("androidx.media3:media3-ui:1.2.1")

	implementation("com.google.accompanist:accompanist-pager:0.24.13-rc")
	implementation("androidx.compose.foundation:foundation:1.6.7")
	implementation("com.google.accompanist:accompanist-pager-indicators:0.24.13-rc")
	implementation("com.google.accompanist:accompanist-systemuicontroller:0.24.13-rc")

	implementation("com.google.code.gson:gson:2.8.8")


	implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")

	// WebSocket Client (OkHttp with WebSocket support)
	implementation("com.squareup.okhttp3:okhttp:4.12.0")
	implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

	// JUnit untuk pengujian unit
	testImplementation("junit:junit:4.13.2")

	// MockK untuk mocking
	testImplementation("io.mockk:mockk:1.13.7")

	// Coroutines Testing
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

	// Truth untuk asertasi
	testImplementation("com.google.truth:truth:1.1.5")

	// Hilt Testing (opsional, jika Hilt digunakan di repository)
	testImplementation("com.google.dagger:hilt-android-testing:2.48.1")
	kaptTest("com.google.dagger:hilt-compiler:2.48.1")


	implementation("com.github.madrapps:plot:0.1.1")

	implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")


	implementation("com.airbnb.android:lottie-compose:6.1.0")

	implementation("com.google.accompanist:accompanist-pager:0.30.1")
	implementation("com.google.accompanist:accompanist-pager-indicators:0.30.1")



}