package com.barbeiroemcasa.util



object ThreadUtil {
    var isRecursionEnable = true

    fun runInBackground(void: () -> Unit) {
        if (!isRecursionEnable) // Handle not to start multiple parallel threads
            return

        Thread(Runnable {
            runInBackground {
                void()
            }
        }).start()

    }
}