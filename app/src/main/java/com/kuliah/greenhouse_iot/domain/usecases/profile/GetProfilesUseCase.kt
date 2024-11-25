package com.kuliah.greenhouse_iot.domain.usecases.profile

import com.kuliah.greenhouse_iot.data.model.controll.auto.Profile
import com.kuliah.greenhouse_iot.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfilesUseCase @Inject constructor(private val repository: ProfileRepository) {
	suspend operator fun invoke(): List<Profile> = repository.getProfiles()
}