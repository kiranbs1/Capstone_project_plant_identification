package com.example.plantification.data

import com.example.plantification.data.requests.ClassifiedJsonReturn
import com.example.plantification.data.requests.ClassifyPostBody
import com.example.plantification.data.requests.RandomImageJsonReturn
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface defining the API endpoints and their corresponding HTTP methods.
 */
interface Api {

    companion object {
        //port and url of server, needs changing per device and wireless network
        const val PORT = "5000"
//        const val URL = "http://10.245.93.121:$PORT/"
        const val URL = "http://192.168.0.9:$PORT/"
    }

    /**
     * Request to get random images of flowers with flower species name.
     *
     * @param num_of_images defines how many images will be returned
     * @return [RandomImageJsonReturn] object representing the response containing random flower images
     */
    @GET("random_images/{num_of_images}")
    suspend fun getImages(
        @Path("num_of_images") num_of_images: Int
    ): RandomImageJsonReturn

    /**
     * Request to classify an image which is uploaded as a form data field.
     *
     * @param image base 64 encoded image string
     * @return [ClassifiedJsonReturn] object representing the response containing classified image data
     */
    @FormUrlEncoded
    @POST("classify")
    suspend fun classifyImage(
        @Field("image") image: String
    ): ClassifiedJsonReturn
}
