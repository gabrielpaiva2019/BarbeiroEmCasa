package com.barbeiroemcasa.ui.login

import android.app.Application
import android.os.Handler
import androidx.lifecycle.*
import com.barbeiroemcasa.infra.ApplicationSession
import com.barbeiroemcasa.model.Barbeiro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BarbeiroLoginViewModel(application: Application):
    AndroidViewModel(application), LifecycleObserver {
    val isLoggedSuccessLiveData = MutableLiveData<Boolean>().apply { value = false }
    val errorMessageLiveData = MutableLiveData<String>().apply {  }


    fun doLogin(editTextLoginEmail: String, editTextLoginSenha: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(editTextLoginEmail, editTextLoginSenha).addOnSuccessListener {
            isLoggedSuccessLiveData.value = true

            getBarbeiroInformacoes()
        }.addOnFailureListener {exception ->
            isLoggedSuccessLiveData.value = false
            errorMessageLiveData.value = exception.localizedMessage
        }
    }

     fun getBarbeiroInformacoes() {
         isLoggedSuccessLiveData.value = false
        val authId = FirebaseAuth.getInstance().uid
        val firebaseDatabase = FirebaseDatabase.getInstance().getReference("usuarios/barbeiros/$authId")
        firebaseDatabase.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val barbeiro = snapshot.getValue(Barbeiro::class.java)
                barbeiro?.let {
                    ApplicationSession.barbeiro = it
                }
                if (barbeiro == null){
                    errorMessageLiveData.value = "Ops... você não possui cadastro de barbeiro, crie uma conta de barbeiro"
                    FirebaseAuth.getInstance().signOut()
                }
            }

        })
    }

}