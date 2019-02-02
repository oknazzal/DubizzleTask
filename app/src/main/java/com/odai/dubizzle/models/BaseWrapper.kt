package com.odai.dubizzle.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
@JsonClass(generateAdapter = true)
data class BaseWrapper<T>(

    @Json(name = "page")
    var page: Int,

    @Json(name = "total_results")
    var totalResults: Int?,

    @Json(name = "total_pages")
    var totalPages: Int?,

    @Json(name = "results")
    var data: T? = null
)
