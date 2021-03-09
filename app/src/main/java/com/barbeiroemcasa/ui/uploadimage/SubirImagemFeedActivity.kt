package com.barbeiroemcasa.ui.uploadimage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.barbeiroemcasa.BaseActivity
import com.barbeiroemcasa.R
import com.barbeiroemcasa.model.Feed
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_subir_imagem_feed.*


class SubirImagemFeedActivity : BaseActivity() {
    var uriImageUpload: Uri? = null

    lateinit var viewModel: SubirImagemFeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subir_imagem_feed)

        iniciaViewModel()
        iniciaObservers()

        selectedImageFeed.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 5)
        }

        buttonSalvar.setOnClickListener {
            when {
                editTextNomeCorte.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "O campo nome do corte está vazio", Toast.LENGTH_SHORT)
                        .show()
                }
                editTextDescricaoCorte.text.isNullOrEmpty() -> {
                    Toast.makeText(
                        this,
                        "O campo descrição do corte está vazio",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                editTextDescricaoCorte.length() > 100 -> {
                    Toast.makeText(
                        this,
                        "O campo descrição do corte não pode conter mais que 100 caracteres",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                uriImageUpload == null -> {
                    Toast.makeText(
                        this,
                        "Clique na imagem acima para selecionar uma imagem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    showLoading()
                    val feed = Feed()
                    feed.descricaoCorte = editTextDescricaoCorte?.text.toString()
                    feed.nomeCorte = editTextNomeCorte?.text.toString()

                    viewModel.saveFeedItem(uriImageUpload, feed)
                }
            }
        }
    }

    private fun iniciaObservers() {
        viewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            hideLoading()
        })
        viewModel.feedSalvo.observe(this, Observer {
            if (it){
                hideLoading()
                Toast.makeText(this, "Sua foto foi salva com sucesso, entre na pagina de feed para visualizar", Toast.LENGTH_LONG).show()
                finish()
            }
        })
    }

    private fun iniciaViewModel() {
        viewModel = getViewModel(SubirImagemFeedViewModel::class.java, application)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {

            this.uriImageUpload = data?.data

            Picasso.with(this)
                .load(data?.data)
                .into(selectedImageFeed)
        }
    }
}
