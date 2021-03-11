package com.barbeiroemcasa.ui

import android.app.Application
import com.barbeiroemcasa.util.AnalyticsUtil
import com.onesignal.OneSignal

class CustomApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);

        OneSignal.setAppId("df1f7c45-a902-4f1d-913c-21e03aec32d7");

    }
}