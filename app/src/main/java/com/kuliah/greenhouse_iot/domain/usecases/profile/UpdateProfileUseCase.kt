package com.kuliah.greenhouse_iot.domain.usecases.profile

import com.kuliah.greenhouse_iot.data.model.controll.auto.Profile
import com.kuliah.greenhouse_iot.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(private val repository: ProfileRepository) {
	suspend operator fun invoke(id: Int, profile: Profile): Profile = repository.updateProfile(id, profile)
}