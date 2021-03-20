package com.barbeiroemcasa.ui.dadoscadastrais

import android.os.Bundle
import android.widget.Toast
import com.barbeiroemcasa.BaseActivity
import com.barbeiroemcasa.R
import com.barbeiroemcasa.model.Barbeiro
import com.barbeiroemcasa.util.AnalyticsUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_dados_cadastrais.*

class DadosCadastraisActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dados_cadastrais)
        showLoading()

        setUpListeners()

        var firebaseDatabase = FirebaseDatabase.getInstance()
            .getReference("usuarios/barbeiros/${FirebaseAuth.getInstance().uid}")

        firebaseDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DadosCadastraisActivity, "Ocorreu um erro, tente novamente mais tarde", Toast.LENGTH_SHORT).show()
                hideLoading()
                finish()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var barbeiro = snapshot.getValue(Barbeiro::class.java)

                configEditTexts(barbeiro)
                hideLoading()
            }

        })
    }

    private fun setUpListeners() {
        var firebaseDatabase = FirebaseDatabase.getInstance()
            .getReference("usuarios/barbeiros/${FirebaseAuth.getInstance().uid}")
        buttonAtualizarCadastro.setOnClickListener {
            if (editTextInstagramBarbeiroPerfil.text.isNullOrEmpty() && editTextNomeBarbeiroPerfil.text.isNullOrEmpty() &&
                    editTextWhatsappBarbeiroPerfil.text.isNullOrEmpty()){

                Toast.makeText(this, "Os campos não podem estar vazios", Toast.LENGTH_SHORT).show()

            }else{
                showLoading()

                var mapUpdateCadastro = HashMap<String, Any>()
                mapUpdateCadastro.put("nomeBarbeiro", editTextNomeBarbeiroPerfil.text.toString())
                mapUpdateCadastro.put("instagramBarbeiro", editTextInstagramBarbeiroPerfil.text.toString())
                mapUpdateCadastro.put("whatsappBarbeiro", editTextWhatsappBarbeiroPerfil.text.toString())

                firebaseDatabase.updateChildren(mapUpdateCadastro).addOnSuccessListener {
                    AnalyticsUtil.track(this, "BARBEIRO_ATUALIZOU_CADASTRO")
                    hideLoading()
                    Toast.makeText(this, "seus dados foram atualizados com sucesso !", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun configEditTexts(barbeiro: Barbeiro?) {

        barbeiro?.let {
            editTextNomeBarbeiroPerfil.setText(barbeiro.nomeBarbeiro)
            editTextInstagramBarbeiroPerfil.setText(barbeiro.instagramBarbeiro)
            editTextWhatsappBarbeiroPerfil.setText(barbeiro.whatsappBarbeiro)
        }
    }
}