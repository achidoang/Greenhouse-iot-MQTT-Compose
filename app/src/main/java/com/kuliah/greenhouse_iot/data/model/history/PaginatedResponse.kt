package com.kuliah.greenhouse_iot.data.model.history

data class PaginatedResponse<T>(
	val data: List<T>,
	val currentPage: Int,
	val totalPages: Int,
	val totalItems: Int
)