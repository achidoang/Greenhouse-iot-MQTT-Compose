package com.kuliah.greenhouse_iot.presentation.viewmodel.guide

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.greenhouse_iot.data.model.guide.Guide
import com.kuliah.greenhouse_iot.domain.usecases.guide.GetGuidesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject constructor(
	private val getGuidesUseCase: GetGuidesUseCase
) : ViewModel() {

	private val _guides = MutableStateFlow<List<Guide>>(emptyList())
	val guides: StateFlow<List<Guide>> = _guides

	init {
		loadGuides()
	}

	fun loadGuides() {
		Log.d("GuideViewModel", "loadGuides() called")
		viewModelScope.launch {
			try {
				_guides.value = getGuidesUseCase()
				Log.d("GuideViewModel", "Guides updated: ${_guides.value}")
			} catch (e: Exception) {
				Log.e("GuideViewModel", "Error loading guides: ${e.message}", e)
			}
		}
	}


}
