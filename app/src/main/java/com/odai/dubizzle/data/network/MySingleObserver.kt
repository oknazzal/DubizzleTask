package com.odai.dubizzle.data.network

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.squareup.moshi.JsonEncodingException
import com.odai.dubizzle.R
import io.reactivex.observers.DisposableSingleObserver
import java.io.IOException

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
abstract class MySingleObserver<T> : DisposableSingleObserver<T>() {

    open fun onError(error: HttpError) {
    }

    final override fun onError(e: Throwable) {
        val error = HttpError()
        when (e) {
            is HttpException -> {
                val code = e.response().code()
                when (code) {
                    UNAUTHENTICATED_ERROR_CODE -> {
                        error.status = HttpStatus.UNAUTHENTICATED_ERROR
                        error.localizedMsg = R.string.unauthenticated_error
                        error.errorIcon = R.drawable.ic_unauthenticated_error
                    }
                    in CLIENT_ERROR_RANGE -> {
                        error.status = HttpStatus.CLIENT_ERROR
                        error.localizedMsg = R.string.client_error
                        error.errorIcon = R.drawable.ic_client_error
                    }
                    in SERVER_ERROR_RANGE -> {
                        error.status = HttpStatus.SERVER_ERROR
                        error.localizedMsg = R.string.server_error
                        error.errorIcon = R.drawable.ic_server_error
                    }
                    else -> {
                        error.status = HttpStatus.UNEXPECTED_ERROR
                        error.localizedMsg = R.string.unexpected_error
                        error.errorIcon = R.drawable.ic_unexpected_error
                    }
                }
            }
            is JsonEncodingException -> {
                error.status = HttpStatus.JSON_ERROR
                error.localizedMsg = R.string.json_error
                error.errorIcon = R.drawable.ic_json_error
            }
            is IOException -> {
                error.status = HttpStatus.NETWORK_ERROR
                error.localizedMsg = R.string.network_error
                error.errorIcon = R.drawable.ic_network_error
            }
            else -> {
                error.status = HttpStatus.UNEXPECTED_ERROR
                error.localizedMsg = R.string.unexpected_error
                error.errorIcon = R.drawable.ic_unexpected_error
            }
        }
        onError(error)
    }
}
