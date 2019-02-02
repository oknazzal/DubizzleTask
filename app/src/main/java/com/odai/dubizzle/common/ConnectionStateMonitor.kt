package com.odai.dubizzle.common

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import javax.inject.Inject

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
class ConnectionStateMonitor @Inject constructor(
    private val context: Context
) : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        val intent = Intent(NETWORK_CHANGE_ACTION)
        intent.putExtra(AVAILABILITY_EXTRA, true)
        context.sendBroadcast(intent)
    }

    override fun onLost(network: Network?) {
        super.onLost(network)
        val intent = Intent(NETWORK_CHANGE_ACTION)
        intent.putExtra(AVAILABILITY_EXTRA, false)
        context.sendBroadcast(intent)
    }

    companion object {

        const val AVAILABILITY_EXTRA = "availability"
        const val NETWORK_CHANGE_ACTION = "com.odai.dubizzle.network.NETWORK_CHANGE"
    }
}
