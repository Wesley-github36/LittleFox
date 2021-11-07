package com.wesleymentoor.reel.connectivitymonitor

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

/**
 * Singleton class that provides executor threads
 * for the network operation and the UI update
 */
class AppExecutors {

    private val diskIO: Executor

    val networkIO: Executor
    val mainThread: Executor

    init {
        networkIO = java.util.concurrent.Executors.newSingleThreadExecutor()
        diskIO = java.util.concurrent.Executors.newFixedThreadPool(3)
        mainThread = MainThreadExecutor()
    }

    companion object {

        @Volatile
        private var instance: AppExecutors? = null

        fun getInstance(): AppExecutors {
            return when {
                instance != null -> instance!!

                else -> {
                    if (instance == null) {
                        synchronized(this) {
                            if (instance == null) {
                                instance = AppExecutors()
                            }
                        }
                    }
                    instance!!
                }
            }
        }

        private class MainThreadExecutor : Executor {
            private val mainThreadHandler = Handler(Looper.getMainLooper())

            override fun execute(command: Runnable) {
                mainThreadHandler.post(command)
            }
        }
    }
}



