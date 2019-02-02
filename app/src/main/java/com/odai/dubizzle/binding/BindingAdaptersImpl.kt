package com.odai.dubizzle.binding

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import javax.inject.Inject

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
class BindingAdaptersImpl @Inject constructor(
    private val glide: RequestManager
) : BindingAdapters {

    override fun ImageView.setImageUrl(url: String?) {
        glide.load(url).into(this)
    }

    override fun View.setViewTransitionName(name: String?) {
        transitionName = name
    }
}
