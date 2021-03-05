package com.barbeiroemcasa.model

class Feed {
    lateinit var nomeCorte: String
    lateinit var descricaoCorte: String
    lateinit var nomeBarbeiro: String
    lateinit var whatsappBarbeiro: String
    lateinit var dataPublicacao: String
    lateinit var horaPublicacao: String
    lateinit var urlFoto: String
    lateinit var uidBarbeiro: String
    var uploadTimestamp = System.currentTimeMillis()
}