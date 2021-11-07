package com.wesleymentoor.reel.connectivitymonitor

import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Class that checks if internet is available
 * after we check if WiFi and mobile data
 * are connected
 */
class NetDetect {

    companion object {
        private lateinit var appExecutors: AppExecutors
    }

    init {
        appExecutors = AppExecutors.getInstance()
    }

    /**
     * Method for establishing a  HTTP URL connection to check
     * for internet connection
     */
    @Synchronized
    fun checkInternetConnection(callback: ConnectivityCallback) {
        appExecutors.networkIO.execute {
            var connection: HttpsURLConnection? = null
            try {
                connection = URL("https://clients3.google.com/generate_204")
                    .openConnection() as HttpsURLConnection
                connection.setRequestProperty("User-Agent" , "Android")
                connection.setRequestProperty("Connection" , "close")
                connection.connectTimeout = 1000
                connection.connect()
                val isConnected =
                    connection.responseCode == 204 && connection.contentLength == 0
                postCallback(callback , isConnected)
                connection.disconnect()
            } catch (e: Exception) {
                postCallback(callback , false)
                connection?.disconnect()
            }
        }
    }

    /**
     * Helper function to post our result to the interface
     * on the main thread
     */
    private fun postCallback(callBack: ConnectivityCallback , isConnected: Boolean) {
        appExecutors.mainThread.execute { callBack.onDetected(isConnected) }
    }

}
