package com.barbeiroemcasa.ui.cadastro.cadastroCliente

class CadastroClienteViewHelper {

    fun getNumeroTelefoneFormatado(stringText: String): String {
        return stringText.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
            .replace("[/]".toRegex(), "").replace("[(]".toRegex(), "").replace("[ ]".toRegex(), "")
            .replace("[:]".toRegex(), "").replace("[)]".toRegex(), "")
    }

}
