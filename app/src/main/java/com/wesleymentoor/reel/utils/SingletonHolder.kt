package com.wesleymentoor.reel.utils

/**
 * Singleton holder for our ConnectionLiveData class
 */
open class SingletonHolder<out T, in Any>(private val constructor: (Any) -> T) {

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: Any): T {
        return when {
            instance != null -> instance!!

            else -> synchronized(this) {
                if (instance == null)
                    instance = constructor(arg)
                instance!!
            }
        }
    }
}


