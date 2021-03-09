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

        view.setOnClickListener {
            showLoading()
        }
    }

    private fun iniciaObservers() {

        viewModel.loginSuccessLiveData.observe(this, Observer { loginSuccess ->
            if (loginSuccess) {
                startActivity(Intent(this, ClienteLogadoActivity::class.java))
            } else {

            }
        })
    }

    private fun iniciaVariaveis() {
        viewModel = getViewModel(HomeViewModel::class.java, application)
    }

    private fun iniciaListeners() {
        buttonSouBarbeiro?.setOnClickListener {
//            startActivity(Intent(this, BarbeiroLoginActivity::class.java))
            showLoading()
        }
        buttonSouCliente?.setOnClickListener {
            if (ApplicationSession.isUsuarioLogado()) {
                startActivity(Intent(this, ClienteLogadoActivity::class.java))
            } else {
                configuraLogin()
            }
        }
    }

    private fun configuraLogin() {
        viewModel.doPrivateLogin()
    }


    override fun onStart() {
        super.onStart()
    }

}