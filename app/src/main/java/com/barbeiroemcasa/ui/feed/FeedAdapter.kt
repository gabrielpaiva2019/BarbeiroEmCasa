package com.barbeiroemcasa.ui.feed

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.barbeiroemcasa.R
import com.barbeiroemcasa.model.Feed
import com.barbeiroemcasa.ui.clientelogado.ClienteLogadoViewHelper
import com.squareup.picasso.Picasso


class FeedAdapter(private var listaFeed: List<Feed>) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        )
    }
    override fun getItemCount(): Int {
        return listaFeed.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        var feed = listaFeed[position]

        holder.textViewTituloVideo.text = feed.nomeCorte
        holder.textViewDescricaoVideo.text = feed.descricaoCorte
        holder.texViewNomeBarbeiro.text = feed.nomeBarbeiro
        holder.textViewDataPublicacao.text = feed.dataPublicacao

        Picasso.with(holder.itemView.context)
            .load(feed.urlFoto)
            .into(holder.imageViewPost)


        configuraEnvioMensagemWhatsapp(holder, feed)

    }

    private fun configuraEnvioMensagemWhatsapp(
        holder: FeedViewHolder,
        feed: Feed
    ) {
        var clienteLogadoViewHelper = ClienteLogadoViewHelper()
        holder.cardViewContatoWhats.setOnClickListener {
            val url =
                "https://api.whatsapp.com/send?phone=${clienteLogadoViewHelper.getWhatsappFormatted(
                    feed.whatsappBarbeiro,
                    holder.itemView.context
                )}"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(holder.itemView.context, i, null)
        }

        holder.frameLayoutWhatsapp.setOnClickListener {
            val url =
                "https://api.whatsapp.com/send?phone=${clienteLogadoViewHelper.getWhatsappFormatted(
                    feed.whatsappBarbeiro,
                    holder.itemView.context
                )}"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(holder.itemView.context, i, null)
        }
    }


    class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var imageViewPost = view.findViewById<ImageView>(R.id.imageViewPost)
        var textViewDataPublicacao = view.findViewById<TextView>(R.id.textViewDataPublicacao)
        var texViewNomeBarbeiro = view.findViewById<TextView>(R.id.texViewNomeBarbeiro)
        var textViewTituloVideo = view.findViewById<TextView>(R.id.textViewTituloVideo)
        var textViewDescricaoVideo = view.findViewById<TextView>(R.id.textViewDescricaoVideo)
        var cardViewContatoWhats = view.findViewById<CardView>(R.id.cardViewContatoWhats)
        var frameLayoutWhatsapp = view.findViewById<FrameLayout>(R.id.frameLayoutWhatsapp)

    }
}
