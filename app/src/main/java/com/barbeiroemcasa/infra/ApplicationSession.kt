package com.barbeiroemcasa.infra

import com.barbeiroemcasa.model.Barbeiro
import com.google.firebase.auth.FirebaseAuth

object ApplicationSession {
   lateinit var barbeiro: Barbeiro


   fun isUsuarioLogado(): Boolean{
      return FirebaseAuth.getInstance().currentUser != null
   }
}