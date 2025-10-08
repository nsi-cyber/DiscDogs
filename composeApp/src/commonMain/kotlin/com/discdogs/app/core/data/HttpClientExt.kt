package com.discdogs.app.core.data

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.ensureActive
import kotlinx.io.IOException
import kotlin.coroutines.coroutineContext


private const val TAG = "ApiResponse"






sealed interface Resource<T> {
    data class Success<T>(
        val value: T,
        val message: String? = null
    ) : Resource<T>

    data class Error<T>(
        val code: Int? = null,
        val message: String? = null,
        val throwable: Throwable? = null,
        val internalError: Boolean = false,
    ) : Resource<T>
}


@Suppress("UNCHECKED_CAST")
fun <T, R> ResultWrapper<T>.toResource(
    mapper: (T) -> R = { it as R }
): Resource<R> {
    return when (this) {
        is ResultWrapper.Success -> this.toResource(mapper)
        is ResultWrapper.GenericError -> this.toResourceError()
        is ResultWrapper.NetworkError -> this.toResourceError()
    }
}

fun <T, R> ResultWrapper.Success<T>.toResource(
    mapper: (T) -> R,
): Resource.Success<R> {
    return Resource.Success(mapper(value), message)
}

fun <T> ResultWrapper.GenericError.toResourceError(): Resource.Error<T> {
    return Resource.Error(code, message, throwable, internalError)
}

fun <T> ResultWrapper.NetworkError.toResourceError(): Resource.Error<T> {
    return Resource.Error(throwable = throwable)
}

sealed class ResultWrapper<out T>  {
    data class Success<out T>(val value: T, val message: String? = null) : ResultWrapper<T>()
    data class GenericError(
        val code: Int? = null,
        val message: String? = null,
        val throwable: Throwable? = null,
        val internalError: Boolean = false,
    ) : ResultWrapper<Nothing>()

    data class NetworkError(val throwable: IOException? = null) : ResultWrapper<Nothing>()
}





suspend inline fun <reified T> safeApiCall(
    crossinline execute: suspend () -> HttpResponse
): ResultWrapper<T> {
    val response = try {
        execute()
    } catch (e: IOException) {
        return ResultWrapper.NetworkError(e)
    } catch (e: Throwable) {
        coroutineContext.ensureActive()
        return ResultWrapper.GenericError(throwable = e)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): ResultWrapper<T> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                val value = response.body<T>()
                ResultWrapper.Success(value)
            } catch (e: NoTransformationFoundException) {
                ResultWrapper.GenericError(
                    message = "Serialization error: ${e.message}",
                    throwable = e
                )
            } catch (e: Exception) {
                ResultWrapper.GenericError(
                    message = "An unexpected error occurred during deserialization.",
                    throwable = e
                )
            }
        }
        else -> {
            ResultWrapper.GenericError(code = response.status.value, message = response.status.description)
        }
    }
}