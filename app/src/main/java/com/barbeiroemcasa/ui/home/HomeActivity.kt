package com.barbeiroemcasa.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.barbeiroemcasa.BaseActivity
import com.barbeiroemcasa.R
import com.barbeiroemcasa.infra.ApplicationSession
import com.barbeiroemcasa.ui.clientelogado.ClienteLogadoActivity
import com.barbeiroemcasa.ui.login.BarbeiroLoginActivity
import com.github.loadingview.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : BaseActivity() {

    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iniciaVariaveis()
        iniciaObservers()
        iniciaListeners()
    }

    private fun iniciaObservers() {

        viewModel.loginSuccessLiveData.observe(this, Observer {loginSuccess ->
            if (loginSuccess){
                finish()
                startActivity(Intent(this, ClienteLogadoActivity::class.java))
            }else{

            }
        })
    }

    private fun iniciaVariaveis() {
        viewModel = getViewModel(HomeViewModel::class.java, application)
    }

    private fun iniciaListeners() {
        buttonSouBarbeiro.setOnClickListener {
            finish()
            startActivity(Intent(this, BarbeiroLoginActivity::class.java))
        }
        buttonSouCliente.setOnClickListener {
            if (ApplicationSession.isUsuarioLogado()){
                finish()
                startActivity(Intent(this, ClienteLogadoActivity::class.java))
            }else{
                configuraLogin()
            }
        }
    }

    private fun configuraLogin() {
        LoadingDialog[this].show()
        finish()
        viewModel.doPrivateLogin()
    }


    override fun onStart() {
        super.onStart()
    }

}