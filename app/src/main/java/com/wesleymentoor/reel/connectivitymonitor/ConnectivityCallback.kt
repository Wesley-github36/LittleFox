package com.wesleymentoor.reel.connectivitymonitor

/**
 * Interface we'll use to communicate with the UI
 */
interface ConnectivityCallback {
    fun onDetected(isConnected: Boolean)
}