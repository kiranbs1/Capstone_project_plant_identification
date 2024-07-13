package com.example.plantification.presentation.classifiedResults

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantification.data.Resource
import com.example.plantification.data.repository.PlantRepository
import com.example.plantification.data.requests.ClassifiedJsonReturn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/**
 * ViewModel class for handling logic related to classified results.
 */
@HiltViewModel
class ClassifiedResultsViewModel @Inject constructor(
    private val plantRepository: PlantRepository
) : ViewModel() {

    // State to hold the classified results
    var state by mutableStateOf(ClassifiedResultsState())
        private set

    /**
     * Function to classify an image using the provided base64 encoded image.
     *
     * @param base64Image Base64 encoded string representing the image.
     */
    fun classifyImage(base64Image: String) {
        viewModelScope.launch {
            // Set loading state
            state = state.copy(
                isLoading = true,
                error = null
            )
            // Perform image classification
            when(val result = plantRepository.postClassify(image = base64Image)) {
                is Resource.Success -> {
                    // Update state with classification results
                    state = state.copy(
                        error = null,
                        isLoading = false,
                        results = result.data,
                        classifiedResults = result.data?.let { transformClassifiedData(it) }
                    )
                }
                is Resource.Error -> {
                    // Update state with error message
                    state.copy(
                        error = result.message,
                        isLoading = false,
                        results = null
                    )
                    println("POST FAILED: " + result.message.toString() )
                }
            }

        }
    }

    /**
     * Function to transform the raw classified data into a structured format ready to be displayed.
     *
     * @param results Raw classified data.
     * @return Structured classified results.
     */
    fun transformClassifiedData(results: ClassifiedJsonReturn) : ClassifiedResults {
        val classifiedResults = ClassifiedResults()
        classifiedResults.class_one = results.class_one
        classifiedResults.class_two = results.class_two
        classifiedResults.class_three = results.class_three
        classifiedResults.class_one_rating = ratingToString(results.class_one_rating)
        classifiedResults.class_two_rating = ratingToString(results.class_two_rating)
        classifiedResults.class_three_rating = ratingToString(results.class_three_rating)
        for (b46image in results.class_one_images) {
            val imageBytes = Base64.decode(b46image, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            classifiedResults.class_one_images.add(decodedImage)
        }
        for (b46image in results.class_two_images) {
            val imageBytes = Base64.decode(b46image, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            classifiedResults.class_two_images.add(decodedImage)
        }
        for (b46image in results.class_three_images) {
            val imageBytes = Base64.decode(b46image, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            classifiedResults.class_three_images.add(decodedImage)
        }
        return classifiedResults
    }

    /**
     * Function to convert a numerical rating to a formatted string.
     *
     * @param num Numerical rating.
     * @return Formatted rating string.
     */
    fun ratingToString(num: Float) : String {
        val scaledNum = num * 100
        val strNum = String.format("%.3f", scaledNum)
        return "$strNum%";
    }

    /**
     * Function to encode an image URI to a base64 string.
     *
     * @param imageUri URI of the image to encode.
     * @param contentResolver ContentResolver instance to read image data.
     * @return Base64 encoded string representing the image.
     */
    fun encodeBase64(imageUri: Uri, contentResolver: ContentResolver): String  {
        try {
            val bytes = contentResolver.openInputStream(imageUri)?.readBytes()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (error: IOException) {
            error.printStackTrace()
        }
        return "error"
    }
}