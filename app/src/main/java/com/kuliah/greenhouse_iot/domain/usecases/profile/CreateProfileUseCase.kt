package com.kuliah.greenhouse_iot.domain.usecases.profile

import com.kuliah.greenhouse_iot.data.model.controll.auto.Profile
import com.kuliah.greenhouse_iot.domain.repository.ProfileRepository
import javax.inject.Inject

class CreateProfileUseCase @Inject constructor(private val repository: ProfileRepository) {
	suspend operator fun invoke(profile: Profile): Profile = repository.createProfile(profile)
}