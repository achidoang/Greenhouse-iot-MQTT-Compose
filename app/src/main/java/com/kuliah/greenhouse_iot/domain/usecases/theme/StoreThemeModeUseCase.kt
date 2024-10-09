package com.kuliah.greenhouse_iot.domain.usecases.theme

import com.kuliah.greenhouse_iot.data.local.PreferenceRepository
import javax.inject.Inject

class StoreThemeModeUseCase @Inject constructor(
	private val prefRepository: PreferenceRepository
) {
	suspend operator fun invoke(themeMode: Boolean ) = prefRepository.storeThemeMode(themeMode)
}