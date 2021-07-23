package com.barbeiroemcasa.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.barbeiroemcasa.BaseActivity
import com.barbeiroemcasa.R
import com.barbeiroemcasa.infra.ApplicationSession
import com.barbeiroemcasa.ui.cadastro.cadastroCliente.CadastroClienteActivity
import com.barbeiroemcasa.ui.clientelogado.ClienteLogadoActivity
import com.barbeiroemcasa.ui.login.BarbeiroLoginActivity
import com.barbeiroemcasa.ui.loginCliente.ClienteLoginActivity
import com.barbeiroemcasa.util.AnalyticsUtil
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

        viewModel.loginSuccessLiveData.observe(this, Observer { loginSuccess ->
            if (loginSuccess) {
                startActivity(Intent(this, ClienteLogadoActivity::class.java))
                hideLoading()
            } else {
                hideLoading()
            }
        })
    }

    private fun iniciaVariaveis() {
        viewModel = getViewModel(HomeViewModel::class.java, application)
    }

    private fun iniciaListeners() {
        buttonSouBarbeiro?.setOnClickListener {
            startActivity(Intent(this, BarbeiroLoginActivity::class.java))
            AnalyticsUtil.track(this, "CLIQUE_BOTAO_SOU_BARBEIRO")
        }
        buttonSouCliente?.setOnClickListener {
            AnalyticsUtil.track(this, "CLIQUE_BOTAO_SOU_CLIENTE")
            startActivity(Intent(this, ClienteLoginActivity::class.java))

        }
    }

    private fun configuraLogin() {
        viewModel.doPrivateLogin()
    }


    override fun onStart() {
        super.onStart()
    }

}