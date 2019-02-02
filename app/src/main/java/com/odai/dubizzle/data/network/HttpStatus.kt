package com.odai.dubizzle.data.network

import androidx.annotation.IntDef

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
@IntDef(
    HttpStatus.SERVER_ERROR,
    HttpStatus.CLIENT_ERROR,
    HttpStatus.JSON_ERROR,
    HttpStatus.NETWORK_ERROR,
    HttpStatus.UNAUTHENTICATED_ERROR,
    HttpStatus.UNEXPECTED_ERROR
)
annotation class HttpStatus {

    companion object {

        const val SERVER_ERROR = 1
        const val CLIENT_ERROR = 2
        const val JSON_ERROR = 3
        const val NETWORK_ERROR = 4
        const val UNAUTHENTICATED_ERROR = 5
        const val UNEXPECTED_ERROR = 6
    }
}
