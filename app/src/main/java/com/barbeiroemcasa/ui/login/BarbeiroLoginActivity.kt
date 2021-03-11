package com.barbeiroemcasa.ui.login

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.barbeiroemcasa.BaseActivity
import com.barbeiroemcasa.R
import com.barbeiroemcasa.extensions.isEmptyOrBlankString
import com.barbeiroemcasa.extensions.stringText
import com.barbeiroemcasa.infra.ApplicationSession
import com.barbeiroemcasa.ui.barbeiroLogado.BarbeiroLogadoActivity
import com.barbeiroemcasa.ui.cadastro.CadastroBarbeiroActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_barbeiro_login.*


class BarbeiroLoginActivity : BaseActivity() {

    lateinit var viewModel: BarbeiroLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barbeiro_login)

        inciaVariaveis()
        iniciaListeners()
        iniciaObservers()

    }

    private fun iniciaObservers() {
        viewModel.isLoggedSuccessLiveData.observe(this, Observer { loginSuccess ->
            if (loginSuccess) {
                hideLoading()
                startActivity(Intent(this, BarbeiroLogadoActivity::class.java))
            }
        })

        viewModel.errorMessageLiveData.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            hideLoading()
        })
    }

    private fun iniciaListeners() {
        buttonLogar?.setOnClickListener {
            if (!editTextLoginEmail.isEmptyOrBlankString() && !editTextLoginSenha.isEmptyOrBlankString()) {
                showLoading("Logando\nAguarde ...")
                viewModel.doLogin(editTextLoginEmail.stringText(), editTextLoginSenha.stringText())
            } else {
                Toast.makeText(this, "os campos não podem estar vazios", Toast.LENGTH_SHORT).show()
            }

        }

        textViewCriarNovaConta?.setOnClickListener {
            startActivity(Intent(this, CadastroBarbeiroActivity::class.java))
        }

    }

    private fun inciaVariaveis() {
        viewModel = getViewModel(BarbeiroLoginViewModel::class.java, application)
    }

    private fun isPermissoesAceitas(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun mostrarDialogIncentivoPermissaoLocalizacao() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(CadastroBarbeiroActivity.PERMISSION_INCENTIVE_MESAGE)
            .setCancelable(false)
            .setPositiveButton("Ok") { _: DialogInterface, _: Int ->
                mostrarPermissoes()
            }

        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun mostrarPermissoes() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            CadastroBarbeiroActivity.LOCATION_REQUEST_CODE
        )
    }

    override fun onStart() {
        super.onStart()
        configuraPermissoes()
        verificaUsuarioJaaLogado()
    }

    private fun verificaUsuarioJaaLogado() {
        if (ApplicationSession.isUsuarioLogado()) {
            if (!isUsuarioAnonimo()) {
                viewModel.getBarbeiroInformacoes()
                startActivity(Intent(this, BarbeiroLogadoActivity::class.java))
            }
        }
    }

    private fun isUsuarioAnonimo() = FirebaseAuth.getInstance().currentUser?.isAnonymous!!

    private fun configuraPermissoes() {
        if (!isPermissoesAceitas()) {
            mostrarDialogIncentivoPermissaoLocalizacao()
        }
    }

}
