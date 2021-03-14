package com.barbeiroemcasa.ui.servicosbarbeiro

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.barbeiroemcasa.R
import com.barbeiroemcasa.infra.ApplicationSession
import com.barbeiroemcasa.model.Barbeiro
import com.barbeiroemcasa.model.ServicoBarbeiro
import com.barbeiroemcasa.ui.clientelogado.ClienteLogadoViewHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_servicos_barbeiro.*
import java.text.NumberFormat
import java.util.*


class ServicosBarbeiroActivity : AppCompatActivity() {

    var uidBarbeiro: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servicos_barbeiro)

        getBundleInfo()

        if (isCliente){
            floatingActionButtonAddServico.visibility = View.GONE
            textViewServicosNomeBarbeiro.text = barbeiro?.nomeBarbeiro
            imageViewAppIcon.setBackgroundResource(R.drawable.ic_whatsapp)


            var clienteWrapper = ClienteLogadoViewHelper()
            imageViewAppIcon.setOnClickListener {
                val url =
                    "https://api.whatsapp.com/send?phone=${clienteWrapper.getWhatsappFormatted(
                        barbeiro?.whatsappBarbeiro!!,
                        this
                    )}"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)



            }

        }



        floatingActionButtonAddServico.setOnClickListener {
            showDialogAddServico()
        }

        configRecyclerView()

    }

    private fun getBundleInfo() {
        uidBarbeiro = barbeiro?.uid

        if (!isCliente){
            uidBarbeiro = FirebaseAuth.getInstance().uid
        }
    }

    private fun configRecyclerView() {
        var uid = FirebaseAuth.getInstance().uid

        var database = FirebaseDatabase.getInstance()
            .getReference("usuarios/barbeiros/$uidBarbeiro/servicos")

        var listServicos = mutableListOf<ServicoBarbeiro>()

        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapShot in snapshot.children) {
                    var servico = datasnapShot.getValue(ServicoBarbeiro::class.java)
                    listServicos.add(servico!!)
                }
                setUpAdapter(listServicos)
            }

        })

    }

    private fun setUpAdapter(listServicos: MutableList<ServicoBarbeiro>) {
        recyclerViewServicos.layoutManager = LinearLayoutManager(this)
        recyclerViewServicos.adapter = ServicosBarbeiroAdapter(listServicos)
    }

    private fun showDialogAddServico() {
        // create an alert builder
        // create an alert builder
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Novo item")
        // set the custom layout
        // set the custom layout
        val customLayout: View = layoutInflater.inflate(R.layout.servicos_custom_dialog, null)

        var editTextAmount = customLayout.findViewById<EditText>(R.id.editTextValorServico)
        var editTextNomeServico = customLayout.findViewById<EditText>(R.id.editTextNomeServico)


        builder.setView(customLayout)
        // add a button
        // add a button
        builder.setPositiveButton("Salvar",
            DialogInterface.OnClickListener { dialog, which -> // send // data from the AlertDialog to the Activity
                var servicoBarbeiro = ServicoBarbeiro()
                servicoBarbeiro.nomeServico = editTextNomeServico.text.toString()
                servicoBarbeiro.precoServico = editTextAmount.text.toString().toDouble()
                sendToFirebase(servicoBarbeiro)
                dialog.dismiss()


            })
        // create and show the alert dialog
        // create and show the alert dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun sendToFirebase(servicoBarbeiro: ServicoBarbeiro) {
        var uid = FirebaseAuth.getInstance().uid

        servicoBarbeiro.barbeiro = ApplicationSession.barbeiro!!
        var randomUid = UUID.randomUUID().toString()
        servicoBarbeiro.uuid = randomUid
        var database = FirebaseDatabase.getInstance()
            .getReference("usuarios/barbeiros/$uid/servicos/$randomUid")
        var databaseAllServicos = FirebaseDatabase.getInstance().getReference("servicos/$randomUid")

        databaseAllServicos.setValue(servicoBarbeiro)

        database.setValue(servicoBarbeiro)
    }


    companion object{
        var isCliente = false
        var barbeiro: Barbeiro? = null
    }

}