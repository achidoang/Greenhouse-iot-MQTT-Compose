package com.kuliah.greenhouse_iot.presentation.viewmodel.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.model.AktuatorData
import com.kuliah.greenhouse_iot.data.model.MonitoringData
import com.kuliah.greenhouse_iot.data.model.SetPointData
import com.kuliah.greenhouse_iot.domain.repository.HistoryRepository
import com.kuliah.greenhouse_iot.domain.usecases.history.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
	private val historyRepository: HistoryRepository
) : ViewModel() {

	val monitoringHistory: LiveData<List<MonitoringData>> = historyRepository.getMonitoringHistory()
		.asLiveData()

	val aktuatorHistory: LiveData<List<AktuatorData>> = historyRepository.getAktuatorHistory()
		.asLiveData()

	val setPointHistory: LiveData<List<SetPointData>> = historyRepository.getSetPointHistory()
		.asLiveData()
}

