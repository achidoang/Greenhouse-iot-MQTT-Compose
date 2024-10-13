package com.kuliah.greenhouse_iot.presentation.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.domain.usecases.theme.RetrieveThemeModeUseCase
import com.kuliah.greenhouse_iot.domain.usecases.theme.StoreThemeModeUseCase
import com.kuliah.greenhouse_iot.util.Constants.BROKER_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
	private val storeThemeModeUseCase: StoreThemeModeUseCase,
	private val retrieveThemeModeUseCase: RetrieveThemeModeUseCase,
) : ViewModel() {

	private val _themeMode = MutableStateFlow(false)
	var themeMode = _themeMode.asStateFlow()


	
	init {
		retrieveThemeMode()
	}

	fun onEvent( event: HomeScreenEvent) {
		when( event ) {
			is HomeScreenEvent.ThemeToggled -> {
				viewModelScope.launch {
					storeThemeModeUseCase.invoke( event.themeMode )
				}
			}
		}
	}

	private fun retrieveThemeMode() {
		viewModelScope.launch {
			retrieveThemeModeUseCase.invoke().collectLatest {
				_themeMode.value = it
			}
		}
	}

}

sealed class HomeScreenEvent() {
	data class ThemeToggled( val themeMode: Boolean ) : HomeScreenEvent()
}