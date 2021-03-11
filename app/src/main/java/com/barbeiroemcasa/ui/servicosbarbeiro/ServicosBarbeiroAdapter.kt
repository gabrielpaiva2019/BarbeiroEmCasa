package com.barbeiroemcasa.ui.servicosbarbeiro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.barbeiroemcasa.R
import com.barbeiroemcasa.model.ServicoBarbeiro
import java.text.NumberFormat
import java.util.*


class ServicosBarbeiroAdapter(private var listaServicos: List<ServicoBarbeiro>) :
    RecyclerView.Adapter<ServicosBarbeiroAdapter.ServicosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicosViewHolder {
        return ServicosViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_servicos, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return listaServicos.size
    }

    override fun onBindViewHolder(holder: ServicosViewHolder, position: Int) {
        var servicosObject = listaServicos[position]

        holder.nomeServico.text = servicosObject.nomeServico
        holder.valorServico.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(servicosObject.precoServico)

    }


    class ServicosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nomeServico = view.findViewById<TextView>(R.id.textViewNomeServico)
        var valorServico = view.findViewById<TextView>(R.id.textViewValorCorte)

    }
}
