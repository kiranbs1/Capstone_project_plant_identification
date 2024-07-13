package com.example.plantification.presentation.mainActivity

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantification.data.Resource
import com.example.plantification.data.repository.PlantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the main activity, responsible for managing UI-related data and actions.
 *
 * @param plantRepository the repository for fetching plant-related data
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val plantRepository: PlantRepository
) : ViewModel(){

    /**
     * Represents the current state of the UI.
     */
    var state by mutableStateOf(MainModelState())
        private set

    /**
     * Loads a set of home images asynchronously.
     */
    fun loadHomeImages() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            when(val result = plantRepository.getRandomImages(9)) {
                is Resource.Success -> {
                    state = state.copy(
                        error = null,
                        isLoading = false,
                        images = result.data?.images
                    )
                }
                is Resource.Error -> {
                    state.copy(
                        images = null,
                        error = result.message,
                        isLoading = false
                    )
                }
            }

        }
    }
}
