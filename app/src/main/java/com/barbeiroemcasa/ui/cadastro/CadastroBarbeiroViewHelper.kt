package com.barbeiroemcasa.ui.cadastro

class CadastroBarbeiroViewHelper {

    fun getNumeroTelefoneFormatado(stringText: String): String {
        return stringText.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
            .replace("[/]".toRegex(), "").replace("[(]".toRegex(), "").replace("[ ]".toRegex(), "")
            .replace("[:]".toRegex(), "").replace("[)]".toRegex(), "")
    }

}
