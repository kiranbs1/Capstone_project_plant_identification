package com.example.plantification.data.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.json.JSONException
import org.json.JSONObject


class ClassifyPostBody(

    val image: String
) {

    //JSONObject automatically adds \ into string so i had to create JSON by hand
    fun toJSON(): String {
        val json = "{\"image\":\"$image\"}"
        println("TO JSON BASE 64 STRING $json")
        return json
    }
}

