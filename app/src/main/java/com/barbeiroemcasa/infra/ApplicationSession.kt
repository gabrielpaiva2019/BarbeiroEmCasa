package com.barbeiroemcasa.infra

import com.barbeiroemcasa.model.Barbeiro
import com.google.firebase.auth.FirebaseAuth

object ApplicationSession {
   var barbeiro: Barbeiro? = null


   fun isUsuarioLogado(): Boolean{
      return FirebaseAuth.getInstance().currentUser != null
   }
}