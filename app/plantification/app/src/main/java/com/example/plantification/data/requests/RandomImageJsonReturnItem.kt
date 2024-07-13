package com.example.plantification.data.requests

import com.squareup.moshi.Json

data class RandomImageJsonReturnItem(
    @field:Json(name = "image")
    val image: List<String>,
    @field:Json(name = "name")
    val name: String
)