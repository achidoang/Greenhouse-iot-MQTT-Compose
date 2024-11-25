package com.kuliah.greenhouse_iot.domain.usecases.profile

import com.kuliah.greenhouse_iot.domain.repository.ProfileRepository
import javax.inject.Inject

class DeleteProfileUseCase @Inject constructor(private val repository: ProfileRepository) {
	suspend operator fun invoke(id: Int) = repository.deleteProfile(id)
}