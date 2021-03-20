package com.barbeiroemcasa.ui.servicosbarbeiro

import android.app.Activity
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.barbeiroemcasa.R
import com.barbeiroemcasa.model.ServicoBarbeiro
import com.barbeiroemcasa.util.AnalyticsUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text
import java.text.NumberFormat
import java.util.*


class ServicosBarbeiroAdapter(
    private var listaServicos: List<ServicoBarbeiro>,
    var onRecyclerUpdate: () -> Unit,
    var activity: Activity,
    var isCliente: Boolean
) :
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
        holder.valorServico.text =
            NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(servicosObject.precoServico)


        if(!isCliente){
            holder.itemView.setOnClickListener {

                var layoutInflater =
                    holder.itemView.context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

                var view = layoutInflater.inflate(R.layout.servicos_custom_dialog_edit, null)
                showDialogAddServico(holder.itemView.context, view, servicosObject)
            }
        }else{
            holder.textView6.visibility = View.GONE
        }

    }


    private fun showDialogAddServico(
        context: Context,
        view: View,
        servicosObject: ServicoBarbeiro
    ) {
        // create an alert builder
        // create an alert builder
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Editar Item")
        // set the custom layout
        // set the custom layou

        var editTextAmount = view.findViewById<EditText>(R.id.editTextValorServico)
        var editTextNomeServico = view.findViewById<EditText>(R.id.editTextNomeServico)

        editTextAmount.setText(servicosObject.precoServico.toInt().toString())
        editTextNomeServico.setText(servicosObject.nomeServico)


        builder.setView(view)
        // add a button
        // add a button
        builder.setPositiveButton("Salvar",
            DialogInterface.OnClickListener { dialog, which -> // send // data from the AlertDialog to the Activity

                var firebaseDatabase = FirebaseDatabase.getInstance()
                    .getReference("usuarios/barbeiros/${FirebaseAuth.getInstance().uid}/servicos/${servicosObject.uuid}")


                var firebaseDatabaseAllServicos =
                    FirebaseDatabase.getInstance().getReference("servicos/${servicosObject.uuid}")


                var mapUpdateCadastro = HashMap<String, Any>()
                mapUpdateCadastro.put("nomeServico", editTextNomeServico.text.toString())
                mapUpdateCadastro.put("precoServico", editTextAmount.text.toString().toDouble())

                firebaseDatabase.updateChildren(mapUpdateCadastro).addOnSuccessListener {
                    firebaseDatabaseAllServicos.updateChildren(mapUpdateCadastro).addOnSuccessListener {
                        Toast.makeText(context, "seus dados foram atualizados com sucesso !", Toast.LENGTH_SHORT).show()
                        onRecyclerUpdate()
                        AnalyticsUtil.track(activity, "atualiza_servico")
                        dialog.dismiss()
                    }
                }


            })

        builder.setNegativeButton("Apagar") { dialogInterface: DialogInterface, i: Int ->
            var firebaseDatabase = FirebaseDatabase.getInstance()
                .getReference("usuarios/barbeiros/${FirebaseAuth.getInstance().uid}/servicos/${servicosObject.uuid}")


            var firebaseDatabaseAllServicos =
                FirebaseDatabase.getInstance().getReference("servicos/${servicosObject.uuid}")

            firebaseDatabase.removeValue().addOnSuccessListener {
                firebaseDatabaseAllServicos.removeValue().addOnSuccessListener {
                    onRecyclerUpdate()
                    Toast.makeText(context, "serviço removido com sucesso !", Toast.LENGTH_SHORT).show()
                    AnalyticsUtil.track(activity, "exclui_servico")
                }
            }


        }
        // create and show the alert dialog
        // create and show the alert dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    class ServicosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nomeServico = view.findViewById<TextView>(R.id.textViewNomeServico)
        var valorServico = view.findViewById<TextView>(R.id.textViewValorCorte)
        var textView6 = view.findViewById<TextView>(R.id.textView6)

    }
}
