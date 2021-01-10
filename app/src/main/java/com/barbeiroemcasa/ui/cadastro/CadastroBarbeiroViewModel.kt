package com.barbeiroemcasa.ui.cadastro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.barbeiroemcasa.infra.ApplicationSession
import com.barbeiroemcasa.model.Barbeiro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class CadastroBarbeiroViewModel(application: Application):
    AndroidViewModel(application), LifecycleObserver {

    var errorLiveData = MutableLiveData<String>()
    var successLiveData = MutableLiveData<Boolean>()

    fun criarNovaContaBarbeiro(
        barbeiroObject: Barbeiro
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(barbeiroObject.emailBarbeiro, barbeiroObject.senhaBarbeiro)
            .addOnCompleteListener { criacaoConta ->
                if (criacaoConta.isSuccessful){
                    barbeiroObject.uid = criacaoConta.result?.user?.uid!!
                    salvarDadosFirebase(barbeiroObject)
                }else{
                    errorLiveData.value = criacaoConta.exception?.localizedMessage
                }
        }
    }

    private fun salvarDadosFirebase(
        barbeiroObject: Barbeiro
    ) {
        val firebaseDatabase = FirebaseDatabase.getInstance().getReference("usuarios/barbeiros/${barbeiroObject.uid}")

        firebaseDatabase.setValue(barbeiroObject).addOnCompleteListener {completionListener ->
            if (completionListener.isSuccessful){
                successLiveData.value = true
                ApplicationSession.barbeiro = barbeiroObject
            }
        } .addOnFailureListener {
            errorLiveData.value = it.localizedMessage
        }
    }


}