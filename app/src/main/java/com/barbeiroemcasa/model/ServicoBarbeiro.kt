package com.barbeiroemcasa.model

class ServicoBarbeiro {
    lateinit var nomeServico: String
    lateinit var uuid: String
    var precoServico: Double = 0.0
    lateinit var barbeiro: Barbeiro
}