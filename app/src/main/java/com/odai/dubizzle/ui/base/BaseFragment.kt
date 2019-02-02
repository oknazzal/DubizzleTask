@file:Suppress("DEPRECATION")

package com.odai.dubizzle.ui.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.odai.dubizzle.R
import com.odai.dubizzle.data.network.HttpError
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
abstract class BaseFragment<T : BaseContract.Presenter> : DaggerFragment(), BaseContract.View {

    private val disabledViews = mutableListOf<View>()

    @set:Inject
    var presenter: T? = null

    private val progress: ProgressDialog? by lazy {
        val dialog = ProgressDialog(context)
        dialog.setMessage(getString(R.string.please_wait))
        dialog
    }
    private var binding: ViewDataBinding? = null
    private var parentView: View? = null
    private var isOnViewInflatedCalled = false

    protected abstract val layoutId: Int

    protected open val isDataBinding: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter?.onCreate()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (parentView == null) {
            if (isDataBinding) {
                binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
                parentView = binding?.root
            } else {
                parentView = inflater.inflate(layoutId, container, false)
            }
            if (userVisibleHint) {
                isOnViewInflatedCalled = true
                onViewVisible(parentView!!)
            }
        }
        return parentView
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && !isOnViewInflatedCalled) {
            isOnViewInflatedCalled = true
            view?.let {
                onViewVisible(it)
            }
        }
        super.setUserVisibleHint(isVisibleToUser)
    }

    protected open fun onViewVisible(view: View) {
        presenter?.onViewInflated()
    }

    override fun onStart() {
        presenter?.onStart()
        super.onStart()
    }

    override fun onResume() {
        presenter?.onResume()
        super.onResume()
    }

    override fun onPause() {
        presenter?.onPause()
        super.onPause()
    }

    override fun onStop() {
        presenter?.onStop()
        super.onStop()
    }

    override fun onDetach() {
        super.onDetach()
        presenter?.onDestroy()
        presenter = null
    }

    override fun onDestroyView() {
        if (view != null) {
            val parentViewGroup = parentView?.parent as? ViewGroup?
            parentViewGroup?.removeView(parentView)
        }
        super.onDestroyView()
    }

    override fun toast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun toast(msgRes: Int) {
        Toast.makeText(context, msgRes, Toast.LENGTH_LONG).show()
    }

    override fun snack(msg: String) {
        parentView?.let { view ->
            if (view.parent != null) {
                Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun snack(msgRes: Int) {
        snack(getString(msgRes))
    }

    override fun snack(msg: String, onClickListener: View.OnClickListener) {
        parentView?.let { view ->
            if (view.parent != null) {
                Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, onClickListener).show()
            }
        }
    }

    override fun snack(msgRes: Int, onClickListener: View.OnClickListener) {
        snack(getString(msgRes), onClickListener)
    }

    override fun showLoginDialog() {}

    override fun disableViews() {
        disableAllViews(parentView as? ViewGroup?)
    }

    override fun enableViews() {
        disabledViews.forEach {
            it.isEnabled = true
        }
        disabledViews.clear()
    }

    override fun handleDataError(error: HttpError) {
    }

    override fun showProgress() {
        progress?.show()
    }

    override fun hideProgress() {
        progress?.hide()
    }

    private fun disableAllViews(viewGroup: ViewGroup?) {
        for (i in 0 until (viewGroup?.childCount ?: 0)) {
            if (viewGroup?.getChildAt(i) is ViewGroup) {
                disableAllViews(viewGroup.getChildAt(i) as? ViewGroup?)
            } else if (viewGroup?.getChildAt(i)?.isEnabled == true) {
                viewGroup.getChildAt(i)?.isEnabled = false
                disabledViews.add(viewGroup.getChildAt(i))
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <B> getBinding(): B? {
        return binding as? B?
    }
}
