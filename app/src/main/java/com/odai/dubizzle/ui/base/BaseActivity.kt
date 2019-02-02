@file:Suppress("DEPRECATION")

package com.odai.dubizzle.ui.base

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.ContentFrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.odai.dubizzle.R
import com.odai.dubizzle.common.ConnectionStateMonitor
import com.odai.dubizzle.data.network.HttpError
import com.odai.dubizzle.utils.isNetworkConnected
import com.odai.dubizzle.utils.statusBarHeight
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
abstract class BaseActivity<T : BaseContract.Presenter> : DaggerAppCompatActivity(),
    BaseContract.View {

    private val disabledViews: MutableList<View> by lazy {
        mutableListOf<View>()
    }
    private val networkChangeReceiver: NetworkChangeBroadcastReceiver by lazy {
        NetworkChangeBroadcastReceiver()
    }

    private val progress: ProgressDialog? by lazy {
        val dialog = ProgressDialog(this)
        dialog.setMessage(getString(R.string.please_wait))
        dialog
    }
    private var transparentStatusBar: Boolean = false
    private var hasBackButton = true
    private var parentView: View? = null
    private var binding: ViewDataBinding? = null
    protected var toolbar: Toolbar? = null
    protected var tvNoInternetConnection: View? = null
    private var title: String? = null

    @set:Inject
    var presenter: T? = null

    @JvmOverloads
    fun setContentView(
        layoutResID: Int,
        hasToolbar: Boolean,
        hasBackButton: Boolean = false,
        isDataBinding: Boolean = false,
        transparentStatusBar: Boolean = false,
        title: String? = null
    ) {
        if (isDataBinding) {
            binding = DataBindingUtil.setContentView(this, layoutResID)
        } else {
            super.setContentView(layoutResID)
        }
        presenter?.onViewInflated()

        val content = findViewById<View>(android.R.id.content)
        tvNoInternetConnection = findViewById(R.id.tvNoInternetConnection)
        if (content is ContentFrameLayout) {
            if (content.childCount > 0) {
                parentView = content.getChildAt(0)
            }
        }
        if (parentView == null) {
            parentView = content
        }

        this.transparentStatusBar = transparentStatusBar
        this.title = title
        this.hasBackButton = hasBackButton
        if (hasToolbar) {
            setSupportActionBar()
        }
        if (transparentStatusBar) {
            setTransparentStatusBar()
        }

        if (!isNetworkConnected()) {
            tvNoInternetConnection?.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter?.onCreate()
    }

    override fun onStart() {
        super.onStart()
        presenter?.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter?.onResume()
        registerReceiver(
            networkChangeReceiver,
            IntentFilter(com.odai.dubizzle.common.ConnectionStateMonitor.NETWORK_CHANGE_ACTION)
        )
    }

    override fun onPause() {
        super.onPause()
        presenter?.onPause()
        unregisterReceiver(networkChangeReceiver)
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDestroy()
        presenter = null
        hideProgress()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> if (hasBackButton) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun toast(msgRes: Int) {
        Toast.makeText(this, msgRes, Toast.LENGTH_LONG).show()
    }

    override fun snack(msg: String) {
        parentView?.let { Snackbar.make(it, msg, Snackbar.LENGTH_LONG).show() }
    }

    override fun snack(msgRes: Int) {
        parentView?.let { Snackbar.make(it, msgRes, Snackbar.LENGTH_LONG).show() }
    }

    override fun snack(msg: String, onClickListener: View.OnClickListener) {
        parentView?.let { view ->
            Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, onClickListener).show()
        }
    }

    override fun snack(msgRes: Int, onClickListener: View.OnClickListener) {
        parentView?.let { view ->
            Snackbar.make(view, msgRes, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, onClickListener).show()
        }
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
    protected fun <B : ViewDataBinding> getBinding(): B? {
        return binding as? B?
    }

    private fun setSupportActionBar() {
        toolbar = findViewById(R.id.toolbar)
        if (toolbar != null) {
            if (title != null) {
                toolbar?.title = title
            }
            setSupportActionBar(toolbar)
            val actionBar = supportActionBar
            if (actionBar != null && hasBackButton) {
                actionBar.setDisplayHomeAsUpEnabled(true)
            }
            if (transparentStatusBar) {
                val params = toolbar?.layoutParams as? MarginLayoutParams?
                params?.topMargin = (params?.topMargin ?: 0) + this.statusBarHeight
            }
        }
    }

    private fun setTransparentStatusBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private inner class NetworkChangeBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.extras?.getBoolean(ConnectionStateMonitor.AVAILABILITY_EXTRA) == true) {
                tvNoInternetConnection?.visibility = View.GONE
            } else {
                tvNoInternetConnection?.visibility = View.VISIBLE
            }
        }
    }
}
