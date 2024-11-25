package com.kuliah.greenhouse_iot.presentation.viewmodel.actuator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.model.subscribe.AktuatorData
import com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt.SubscribeAktuatorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AktuatorViewModel @Inject constructor(
	subscribeAktuatorUseCase: SubscribeAktuatorUseCase
) : ViewModel() {

	val aktuatorData: StateFlow<AktuatorData?> = subscribeAktuatorUseCase()
		.distinctUntilChanged() // Emit only if data has changed
		.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

	init {
		viewModelScope.launch {
			aktuatorData.collect { data ->
				Log.d("AktuatorViewModel", "Data received in AktuatorViewModel: $data")
			}
		}
	}
}
