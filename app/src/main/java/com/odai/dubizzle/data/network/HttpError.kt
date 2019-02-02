package com.odai.dubizzle.data.network

import com.odai.dubizzle.models.BaseWrapper

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
const val UNAUTHENTICATED_ERROR_CODE = 401

const val CLIENT_ERROR_RANGE_START = 400
const val CLIENT_ERROR_RANGE_END = 499
val CLIENT_ERROR_RANGE = CLIENT_ERROR_RANGE_START..CLIENT_ERROR_RANGE_END

const val SERVER_ERROR_RANGE_START = 500
const val SERVER_ERROR_RANGE_END = 599
val SERVER_ERROR_RANGE = SERVER_ERROR_RANGE_START..SERVER_ERROR_RANGE_END

class HttpError {

    var code: Int = 0
    var status: Int = 0
    var error: BaseWrapper<*>? = null
    var errorTitle: Int = 0
    var errorIcon: Int = 0
    var localizedMsg: Int = 0
}
