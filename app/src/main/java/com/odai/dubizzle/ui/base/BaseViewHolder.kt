package com.odai.dubizzle.ui.base

import android.view.View

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
abstract class BaseViewHolder<M> : RecyclerView.ViewHolder {

    var viewDataBinding: ViewDataBinding? = null

    constructor(itemView: View) : super(itemView)

    constructor(viewDataBinding: ViewDataBinding) : super(viewDataBinding.root) {
        this.viewDataBinding = viewDataBinding
    }

    abstract fun bind(position: Int, item: M?)

    @Suppress("UNCHECKED_CAST")
    inline fun <T : ViewDataBinding> bind(binding: T.() -> Unit) {
        binding(viewDataBinding as T)
        viewDataBinding?.executePendingBindings()
    }
}
