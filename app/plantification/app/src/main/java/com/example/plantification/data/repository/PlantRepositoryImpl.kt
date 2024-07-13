package com.example.plantification.data.repository

import com.example.plantification.data.Api
import com.example.plantification.data.requests.ClassifiedJsonReturn
import com.example.plantification.data.requests.RandomImageJsonReturn
import com.example.plantification.data.Resource
import javax.inject.Inject

/**
 * Implementation of [PlantRepository] that interacts with the [Api] to perform repository operations.
 *
 * @param api the API singleton interface for network requests injected in
 */
class PlantRepositoryImpl @Inject constructor(
    private val api: Api
) : PlantRepository{

    /**
     * Retrieves a list of random flower images along with their species names from the API.
     *
     * @param num_of_images the number of random images to fetch
     * @return a [Resource] containing the response data or an error message
     */
    override suspend fun getRandomImages(num_of_images: Int): Resource<RandomImageJsonReturn> {
        return try {
            Resource.Success(
                data = api.getImages(
                    num_of_images = num_of_images
                )
            )
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error has occured")
        }
    }

    /**
     * sends image to server to request classification on image. returned is top
     * 3 predictions, thier name, confidence score and reference images
     *
     * @param image base64 encoded image
     * @return a [Resource] containing the response data or an error message
     */
    override suspend fun postClassify(image: String): Resource<ClassifiedJsonReturn> {
        return try {
            Resource.Success(
                data = api.classifyImage(
                    image = image
                )
            )
        } catch (e: Exception) {
            Resource.Error(e.stackTraceToString())
        }
    }
}