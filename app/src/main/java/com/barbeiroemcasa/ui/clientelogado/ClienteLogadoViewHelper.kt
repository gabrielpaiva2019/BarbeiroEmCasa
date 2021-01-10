package com.barbeiroemcasa.ui.clientelogado

import android.content.Context

class ClienteLogadoViewHelper {

    fun getWhatsappFormatted(whatsappBarbeiro: String, context: Context): String{
        val countryCode: String = context.resources.configuration.locale.country
        return countryCode + whatsappBarbeiro
    }
}