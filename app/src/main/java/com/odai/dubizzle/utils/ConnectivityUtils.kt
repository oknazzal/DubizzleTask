package com.odai.dubizzle.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
fun Context.isNetworkConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?
    return cm?.activeNetworkInfo?.isConnected == true
}
