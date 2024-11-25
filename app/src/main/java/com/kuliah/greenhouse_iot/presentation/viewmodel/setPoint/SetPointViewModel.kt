package com.kuliah.greenhouse_iot.presentation.viewmodel.setPoint

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.model.subscribe.SetPointData
import com.kuliah.greenhouse_iot.domain.usecases.subscribe_mqtt.SubscribeSetPointUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SetPointViewModel @Inject constructor(
	subscribeSetPointUseCase: SubscribeSetPointUseCase
) : ViewModel() {

	val setPointData: StateFlow<SetPointData?> = subscribeSetPointUseCase()
		.distinctUntilChanged() // Emit only if data has changed
		.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

	init {
		viewModelScope.launch {
			setPointData.collect { data ->
				Log.d("SetPointViewModel", "Data received in SetPointViewModel: $data")
			}
		}
	}
}
