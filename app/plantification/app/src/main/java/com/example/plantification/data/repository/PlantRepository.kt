package com.example.plantification.data.repository

import com.example.plantification.data.requests.ClassifiedJsonReturn
import com.example.plantification.data.requests.ClassifyPostBody
import com.example.plantification.data.requests.RandomImageJsonReturn
import com.example.plantification.data.Resource

interface PlantRepository {
    suspend fun getRandomImages(num_of_images: Int) : Resource<RandomImageJsonReturn>
    suspend fun postClassify(image : String) : Resource<ClassifiedJsonReturn>

}