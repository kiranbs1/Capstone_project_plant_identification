package com.example.plantification.presentation.classifiedResults

import android.net.Uri
import com.example.plantification.data.requests.ClassifiedJsonReturn

data class ClassifiedResultsState (
    val uri: Uri? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val results: ClassifiedJsonReturn? = null,
    val classifiedResults: ClassifiedResults? = null
    )