package com.odai.dubizzle.ui.filter

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.odai.dubizzle.R
import kotlinx.android.synthetic.main.dialog_filter.*

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
class FilterDialog(
    context: Context,
    private var minYear: Number?,
    private var maxYear: Number?,
    private val onDoneClicked: (
        minYear: Number?,
        maxYear: Number?
    ) -> Unit
) : AppCompatDialog(context, R.style.FilterDialogStyle) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_filter)
        setTitle(R.string.title_menu_activity_main_filter)

        rsbDialogFilterYears.setMinStartValue(minYear?.toFloat() ?: 0f)
        rsbDialogFilterYears.setMaxStartValue(maxYear?.toFloat() ?: 0f)

        rsbDialogFilterYears.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            this.minYear = minValue
            this.maxYear = maxValue
            tvDialogFilterMin.text = minValue.toString()
            tvDialogFilterMax.text = maxValue.toString()
        }

        btnDialogFilterDone?.setOnClickListener {
            onDoneClicked(minYear, maxYear)
            dismiss()
        }
    }
}