package com.wesleymentoor.reel.connectivitymonitor

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import com.wesleymentoor.reel.utils.SingletonHolder

class ConnectionLiveData private constructor(context: Context) : LiveData<Boolean>() {

    private var connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    private val networkRequestBuilder: NetworkRequest.Builder = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)

    override fun onActive() {
        super.onActive()
        updateConnection()

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ->
                connectivityManager.registerDefaultNetworkCallback(getConnectivityMarshmallowManagerCallback())
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
                marshmallowNetworkAvailableRequest()
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun marshmallowNetworkAvailableRequest() {
        connectivityManager.registerNetworkCallback(
            networkRequestBuilder.build() , getConnectivityMarshmallowManagerCallback()
        )
    }

    private fun getConnectivityMarshmallowManagerCallback(): ConnectivityManager.NetworkCallback {
        connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onCapabilitiesChanged(network: Network , networkCapabilities: NetworkCapabilities) {
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                ) {
                    postValue(true)
                }
            }

            override fun onLost(network: Network) {
                postValue(false)
            }
        }
        return connectivityManagerCallback
    }

    private fun updateConnection() {
        val isConnected = isConnected()
        postValue(isConnected)
    }

    private fun isConnected(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        return networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH))
    }

    companion object : SingletonHolder<ConnectionLiveData, Context>(::ConnectionLiveData)

}

