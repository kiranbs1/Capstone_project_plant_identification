package com.example.plantification.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {

    /**
     * Provides a singleton instance of the PlantApi interface for network calls related to plants.
     *
     * @return A singleton instance of the PlantApi interface.
     */
    @Provides
    @Singleton
    fun providePlantApi() : Api {
        // Create an interceptor to log HTTP request and response data
        val interceptor= HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        // Create an OkHttpClient instance with the interceptor attached
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        // Create a Retrofit instance with base URL and converter factory
        return Retrofit.Builder()
            .baseUrl(Api.URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

}