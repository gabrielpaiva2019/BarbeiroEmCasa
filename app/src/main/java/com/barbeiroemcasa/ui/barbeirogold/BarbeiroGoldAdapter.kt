package com.barbeiroemcasa.ui.barbeirogold

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.SkuDetails
import com.barbeiroemcasa.R

class BarbeiroGoldAdapter(
    var listSkus: List<SkuDetails>,
    var billingClient: BillingClient,
    var barbeiroGoldActivity: BarbeiroGoldActivity
) :
    RecyclerView.Adapter<BarbeiroGoldAdapter.BarbeiroGoldViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarbeiroGoldViewHolder {
        return BarbeiroGoldViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_servicos, parent, false)
        )
    }

    override fun getItemCount(): Int = listSkus.size

    override fun onBindViewHolder(holder: BarbeiroGoldViewHolder, position: Int) {
        var produto = listSkus[position]

        holder.nomeServico.text = "Barbeiro Gold 1 Mês"
        holder.valorServico.text = produto.originalPrice

        holder.itemView.setOnClickListener {
            val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(produto)
                .build()
            val responseCode = billingClient.launchBillingFlow(barbeiroGoldActivity, flowParams)

        }

    }

    class BarbeiroGoldViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nomeServico = view.findViewById<TextView>(R.id.textViewNomeServico)
        var valorServico = view.findViewById<TextView>(R.id.textViewValorCorte)

    }
}