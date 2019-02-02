@file:JvmName("ViewUtils")

package com.odai.dubizzle.utils

import android.content.Context
import android.view.View
import com.odai.dubizzle.ui.base.BaseAdapter
import com.odai.dubizzle.ui.base.BaseViewHolder

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */

const val CLICK_DELAY = 1000L

val Context.statusBarHeight: Int
    get() {
        var result = 0
        val resourceId = resources.getIdentifier(
            "status_bar_height",
            "dimen",
            "android"
        )
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

inline fun <T, V : BaseViewHolder<T>> BaseAdapter<T, V>.click(
    crossinline callback: (View, T, position: Int) -> Unit
) {
    setOnItemClickListener { view, any, position ->
        callback(view, any, position)
        view.isEnabled = false
        view.postDelayed({
            view.isEnabled = true
        }, CLICK_DELAY)
    }
}

fun <T, V : BaseViewHolder<T>> BaseAdapter<T, V>.noDataButtonClick(callback: (View) -> Unit) {
    setNoDataButtonClickListener(callback)
}

fun <T, V : BaseViewHolder<T>> BaseAdapter<T, V>.failedButtonClick(callback: (View) -> Unit) {
    setFailedButtonClickListener(callback)
}

fun <T, V : BaseViewHolder<T>> BaseAdapter<T, V>.noDataOrFailedButtonClick(
    callback: (View) -> Unit
) {
    failedButtonClick(callback)
    noDataButtonClick(callback)
}