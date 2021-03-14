package com.barbeiroemcasa.ui.clientelogado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.barbeiroemcasa.R
import com.barbeiroemcasa.model.Barbeiro
import com.barbeiroemcasa.ui.servicosbarbeiro.ServicosBarbeiroActivity


class ClienteLogadoAdapter(private var listaBarbeiros: List<Barbeiro>) : RecyclerView.Adapter<ClienteLogadoAdapter.ClienteLogadoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteLogadoViewHolder {
        return ClienteLogadoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_barbeiros, parent, false))
    }


    override fun getItemCount(): Int {
       return listaBarbeiros.size
    }

    override fun onBindViewHolder(holder: ClienteLogadoViewHolder, position: Int) {
        var barbeiroObject = listaBarbeiros[position]
        holder.textViewNomeBarbeiro.text = barbeiroObject.nomeBarbeiro

        configuraEnvioMensagemWhatsapp(holder, barbeiroObject)

    }

    private fun configuraEnvioMensagemWhatsapp(
        holder: ClienteLogadoViewHolder,
        barbeiroObject: Barbeiro
    ) {
        var clienteLogadoViewHelper = ClienteLogadoViewHelper()
        holder.textViewContatoBarbeiro.setOnClickListener {


            ServicosBarbeiroActivity.barbeiro = barbeiroObject
            ServicosBarbeiroActivity.isCliente = true

            var intent = Intent(holder.itemView.context, ServicosBarbeiroActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }


    class ClienteLogadoViewHolder(view:View): RecyclerView.ViewHolder(view) {

        var textViewNomeBarbeiro = view.findViewById<TextView>(R.id.textViewNomeBarbeiro)
        var textViewContatoBarbeiro = view.findViewById<TextView>(R.id.textViewEntrarContatoBarbeiro)

    }
}
