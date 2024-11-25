package com.kuliah.greenhouse_iot.domain.usecases.profile

import com.kuliah.greenhouse_iot.domain.repository.ProfileRepository
import javax.inject.Inject

class ActivateProfileUseCase @Inject constructor(private val repository: ProfileRepository) {
	suspend operator fun invoke(id: Int) = repository.activateProfile(id)
}