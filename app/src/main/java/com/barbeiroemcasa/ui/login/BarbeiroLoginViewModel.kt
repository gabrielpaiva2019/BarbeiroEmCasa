package com.barbeiroemcasa.ui.login

import android.app.Application
import android.os.Handler
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth

class BarbeiroLoginViewModel(application: Application):
    AndroidViewModel(application), LifecycleObserver {
    val isLoggedSuccessLiveData = MutableLiveData<Boolean>().apply { value = false }
    val errorMessageLiveData = MutableLiveData<String>().apply {  }


    fun doLogin(editTextLoginEmail: String, editTextLoginSenha: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(editTextLoginEmail, editTextLoginSenha).addOnSuccessListener {
            isLoggedSuccessLiveData.value = true
        }.addOnFailureListener {exception ->
            isLoggedSuccessLiveData.value = false
            errorMessageLiveData.value = exception.localizedMessage
        }
    }

}