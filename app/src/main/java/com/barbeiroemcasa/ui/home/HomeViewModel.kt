package com.barbeiroemcasa.ui.home

import android.app.Application
import android.os.Handler
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel(application: Application):
    AndroidViewModel(application), LifecycleObserver {
    val loginSuccessLiveData = MutableLiveData<Boolean>().apply { value = false }


    fun doPrivateLogin() {
        FirebaseAuth.getInstance().signInAnonymously().addOnSuccessListener {
            loginSuccessLiveData.value = true
        }.addOnFailureListener {
            loginSuccessLiveData.value = false
        }
    }

}