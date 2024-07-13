package com.example.plantification.data.requests

import com.squareup.moshi.Json

data class ClassifiedJsonReturn(
    @field:Json(name = "class_one")
    val class_one: String,
    @field:Json(name = "class_one_rating")
    val class_one_rating: Float,
    @field:Json(name = "class_one_images")
    val class_one_images: List<String>,
    @field:Json(name = "class_two")
    val class_two: String,
    @field:Json(name = "class_two_rating")
    val class_two_rating: Float,
    @field:Json(name = "class_two_images")
    val class_two_images: List<String>,
    @field:Json(name = "class_three")
    val class_three: String,
    @field:Json(name = "class_three_rating")
    val class_three_rating: Float,
    @field:Json(name = "class_three_images")
    val class_three_images: List<String>,
)