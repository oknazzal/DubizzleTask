package com.odai.dubizzle.common

import android.net.ConnectivityManager
import android.net.NetworkRequest
import androidx.databinding.DataBindingUtil
import com.odai.dubizzle.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
class MyApplication : DaggerApplication() {

    @set:Inject
    lateinit var networkRequest: NetworkRequest

    @set:Inject
    lateinit var connectionStateMonitor: ConnectionStateMonitor

    @set:Inject
    lateinit var connectivityManager: ConnectivityManager

    override fun onCreate() {
        super.onCreate()
        instance = this

        connectivityManager.registerNetworkCallback(networkRequest, connectionStateMonitor)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        DataBindingUtil.setDefaultComponent(appComponent)
        return appComponent
    }

    companion object {
        lateinit var instance: MyApplication
    }
}
