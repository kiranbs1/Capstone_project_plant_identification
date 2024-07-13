package com.example.plantification.data.requests

import com.squareup.moshi.Json

data class RandomImageJsonReturn(
    @field:Json(name = "plants")
    val images: List<RandomImageJsonReturnItem>
)