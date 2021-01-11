package com.idslogic.levelshoes.network

/**
 * Network statuses to manage response accordingly
 */
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

/**
 * Wrapper class to manage response results
 * @param status - enum representing response status
 * @param data - generic data requested by API call
 * @param message - string representing error message
 * @param code - int representing API call response code
 * Created by eduardsdenisjonoks  on 8/14/18.
 */
class Resource<T> constructor(
        val status: Status,
        val data: T?,
        val message: String?,
        val code: Int?) {

    companion object {
        @JvmStatic
        fun <T> success(data: T?, code: Int?): Resource<T> =
                Resource(Status.SUCCESS, data, null, code)
        @JvmStatic
        fun <T> error(message: String?, data: T?, code: Int = -1): Resource<T> =
                Resource(Status.ERROR, data, message, code)
        @JvmStatic
        fun <T> error(): Resource<T> =
            Resource(Status.ERROR, null, null, 401)

        fun <T> loading(data: T?): Resource<T> = Resource(Status.LOADING, data, null, -1)

        @JvmStatic
        fun <T> loading(): Resource<T> = loading(null)
    }
}