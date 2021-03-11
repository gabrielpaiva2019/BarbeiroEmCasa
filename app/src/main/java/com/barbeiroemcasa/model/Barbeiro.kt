package com.barbeiroemcasa.model

import java.io.Serializable

open class Barbeiro : Serializable{
    lateinit var nomeBarbeiro: String
    lateinit var whatsappBarbeiro: String
    lateinit var instagramBarbeiro: String
    lateinit var emailBarbeiro: String
    lateinit var senhaBarbeiro: String
    lateinit var uid: String
    

    override fun toString(): String {
        return "Barbeiro(nomeBarbeiro='$nomeBarbeiro', whatsappBarbeiro='$whatsappBarbeiro', instagramBarbeiro='$instagramBarbeiro', emailBarbeiro='$emailBarbeiro', senhaBarbeiro='$senhaBarbeiro', uid='$uid')"
    }


}