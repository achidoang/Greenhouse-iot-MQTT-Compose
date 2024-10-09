package com.kuliah.greenhouse_iot.domain.usecases.theme

import com.kuliah.greenhouse_iot.data.local.PreferenceRepository
import javax.inject.Inject

class RetrieveThemeModeUseCase @Inject constructor(
	private val prefRepository: PreferenceRepository
) {
	operator fun invoke() = prefRepository.storedThemeModel
}