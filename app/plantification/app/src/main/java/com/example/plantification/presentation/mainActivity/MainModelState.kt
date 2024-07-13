package com.example.plantification.presentation.mainActivity

import com.example.plantification.data.requests.RandomImageJsonReturnItem

data class MainModelState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val images: List<RandomImageJsonReturnItem>? = null
)