package com.example.plantification.data

/**
 * A sealed class representing a generic resource that can hold either data of type [T] or an error message.
 *
 * @param T the type of data held by this resource
 * @property data the data held by this resource, can be null
 * @property message an optional error message, can be null
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    /**
     * Represents a successful resource containing data of type [T].
     *
     * @param data the data to be held by this resource
     */
    class Success<T>(data: T?): Resource<T>(data)

    /**
     * Represents an error resource containing an error message and optional data of type [T].
     *
     * @param message the error message
     * @param data optional data to be held by this resource
     */
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
}
