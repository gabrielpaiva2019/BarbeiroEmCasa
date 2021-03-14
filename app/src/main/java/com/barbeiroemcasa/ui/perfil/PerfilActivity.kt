package com.barbeiroemcasa.ui.perfil

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.barbeiroemcasa.R
import com.barbeiroemcasa.infra.ApplicationSession
import com.barbeiroemcasa.ui.servicosbarbeiro.ServicosBarbeiroActivity
import kotlinx.android.synthetic.main.activity_perfil.*

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        textViewNomeBarbeiroPerfil.text = getFormattedBarbeiroNome()

        cardViewPerfilMeusServicos.setOnClickListener {
            startActivity(Intent(this, ServicosBarbeiroActivity::class.java))
        }

        cardViewDadosCadastrais.setOnClickListener {
            Toast.makeText(this, "em breve", Toast.LENGTH_SHORT).show()
        }

    }


    private fun getFormattedBarbeiroNome(): String {
        return if (ApplicationSession.barbeiro?.nomeBarbeiro?.length == 23) {
            ApplicationSession.barbeiro?.nomeBarbeiro?.subSequence(0, 20).toString() + "..."
        } else {
            ApplicationSession.barbeiro?.nomeBarbeiro!!
        }
    }

}