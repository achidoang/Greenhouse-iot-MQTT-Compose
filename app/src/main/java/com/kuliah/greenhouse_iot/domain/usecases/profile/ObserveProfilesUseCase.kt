package com.kuliah.greenhouse_iot.domain.usecases.profile

import com.kuliah.greenhouse_iot.data.model.controll.auto.Profile
import com.kuliah.greenhouse_iot.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveProfilesUseCase @Inject constructor(private val repository: ProfileRepository) {
	operator fun invoke(): Flow<List<Profile>> = repository.observeProfiles()
}