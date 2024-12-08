package com.kuliah.greenhouse_iot.domain.usecases.mode

import com.kuliah.greenhouse_iot.data.model.mode.ModeResponse
import com.kuliah.greenhouse_iot.domain.repository.ModeRepository
import javax.inject.Inject

class PostModeUseCase @Inject constructor(
	private val modeRepository: ModeRepository
) {
	suspend operator fun invoke(automode: Int): Result<ModeResponse> {
		return modeRepository.postMode(automode)
	}
}
