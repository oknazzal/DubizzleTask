package com.odai.dubizzle.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.odai.dubizzle.R

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
open class BaseRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    var emptyMsg: String? = null
    var emptyIcon: Int = R.drawable.ic_no_data
    var emptyButtonText: String? = null

    var failedButtonText: String? = context.getString(R.string.retry)

    init {
        val viewAttrs = context.obtainStyledAttributes(
            attrs,
            R.styleable.BaseRecyclerView,
            defStyle,
            0
        )
        emptyMsg = viewAttrs.getString(R.styleable.BaseRecyclerView_empty_msg)
        emptyIcon = viewAttrs.getResourceId(
            R.styleable.BaseRecyclerView_empty_icon,
            R.drawable.ic_no_data
        )
        emptyButtonText = viewAttrs.getString(R.styleable.BaseRecyclerView_empty_button_text)

        if (viewAttrs.hasValue(R.styleable.BaseRecyclerView_failed_button_text)) {
            failedButtonText = viewAttrs.getString(
                R.styleable.BaseRecyclerView_failed_button_text
            )
        }
        viewAttrs.recycle()
    }
}
