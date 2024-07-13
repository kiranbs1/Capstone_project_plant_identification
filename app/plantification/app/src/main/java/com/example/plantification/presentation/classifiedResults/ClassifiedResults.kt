package com.example.plantification.presentation.classifiedResults

import android.graphics.Bitmap
class ClassifiedResults(
    var class_one: String? = null,
    var class_one_rating: String? = null,
    val class_one_images: MutableList<Bitmap> = mutableListOf(),
    var class_two: String? = null,
    var class_two_rating: String? = null,
    val class_two_images: MutableList<Bitmap> = mutableListOf(),
    var class_three: String? = null,
    var class_three_rating: String? = null,
    val class_three_images: MutableList<Bitmap> = mutableListOf(),
    ) {
}
