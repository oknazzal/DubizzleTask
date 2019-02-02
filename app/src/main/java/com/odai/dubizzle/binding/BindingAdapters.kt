package com.odai.dubizzle.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
interface BindingAdapters {

    @BindingAdapter("imageUrl")
    fun ImageView.setImageUrl(url: String?)

    @BindingAdapter("android:transitionName")
    fun View.setViewTransitionName(name: String?)
}
