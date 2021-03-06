package com.barbeiroemcasa.util

import android.R.id
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth


object AnalyticsUtil {

    fun track(context: Context, origin: String){
        val analytics = FirebaseAnalytics.getInstance(context)

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FirebaseAuth.getInstance().uid)
        bundle.putString(FirebaseAnalytics.Param.ORIGIN, origin)
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
    }


}