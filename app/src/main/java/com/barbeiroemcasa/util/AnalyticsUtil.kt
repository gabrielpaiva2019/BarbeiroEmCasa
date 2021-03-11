package com.barbeiroemcasa.util

import android.R.id
import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth


object AnalyticsUtil {

    fun track(context: Activity, tag: String){
        val analytics = FirebaseAnalytics.getInstance(context)

        val bundle = Bundle()
        bundle.putString("USER_UID", FirebaseAuth.getInstance().uid)
        bundle.putString("TELA", context.localClassName)
        analytics.logEvent(tag, bundle)
        analytics.setUserId(FirebaseAuth.getInstance().uid)
    }


    fun track(tag:String){

    }

}