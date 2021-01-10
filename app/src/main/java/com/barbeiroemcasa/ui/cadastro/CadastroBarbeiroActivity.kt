package com.barbeiroemcasa.ui.cadastro

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.barbeiroemcasa.BaseActivity
import com.barbeiroemcasa.R
import com.barbeiroemcasa.extensions.stringText
import com.barbeiroemcasa.infra.ApplicationSession
import com.barbeiroemcasa.model.Barbeiro
import com.barbeiroemcasa.model.LatLng
import com.barbeiroemcasa.ui.barbeiroLogado.BarbeiroLogadoActivity
import com.barbeiroemcasa.util.MaskEditUtil
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cadastro_barbeiro.*


class CadastroBarbeiroActivity : BaseActivity(), LocationListener {

    private lateinit var viewModel: CadastroBarbeiroViewModel
    private lateinit var location: LocationManager
    private lateinit var currentLatlngUser: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_barbeiro)

        inicializaVariaveis()
        inicializaListeners()
        inicializaObservers()
        inicializaViews()

    }

    private fun inicializaViews() {
        editTextWhatsappBarbeiro.addTextChangedListener(MaskEditUtil.mask(editTextWhatsappBarbeiro, MaskEditUtil.FORMAT_FONE))
    }

    private fun inicializaObservers() {
        viewModel.successLiveData.observe(this, Observer {isSucesso ->
            if (isSucesso){
                mostrarTelaBarbeiroLogado()
            }
        })
    }

    private fun mostrarTelaBarbeiroLogado() {
        startActivity(Intent(this, BarbeiroLogadoActivity::class.java))
    }

    private fun inicializaListeners() {
        buttonCadastrarBarbeiro.setOnClickListener {
            if (isPermissoesAceitas()){
                if (!editTextEmailBarbeiro.text.isNullOrBlank() && !editTextInstagramBarbeiro.text.isNullOrBlank() && !editTextNomeBarbeiro.text.isNullOrBlank() != null &&
                    !editTextSenhaBarbeiro.text.isNullOrBlank() != null && !editTextWhatsappBarbeiro.text.isNullOrBlank() != null) {

                    viewModel.criarNovaContaBarbeiro(getBarbeiroObject())
                } else {
                    Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_LONG).show()
                }
            }else{
                mostrarDialogIncentivoPermissaoLocalizacao()
            }

        }
    }

    private fun getBarbeiroObject(): Barbeiro {
        var barbeiro = Barbeiro()
        barbeiro.nomeBarbeiro = editTextNomeBarbeiro.stringText()
        barbeiro. whatsappBarbeiro = editTextWhatsappBarbeiro.stringText()
        barbeiro.instagramBarbeiro = editTextInstagramBarbeiro.stringText()
        barbeiro.emailBarbeiro = editTextEmailBarbeiro.stringText()
        barbeiro.senhaBarbeiro = editTextSenhaBarbeiro.stringText()
        barbeiro.uid = ""
        return Barbeiro()
    }

    private fun inicializaVariaveis() {
        location = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        viewModel = getViewModel(viewModelClass = CadastroBarbeiroViewModel::class.java,
            application = application)
    }

    override fun onStart() {
        super.onStart()
        verificaPermissoesLocalizacao()
        verificaUsuarioLogado()
    }

    private fun verificaUsuarioLogado() {
        if (ApplicationSession.isUsuarioLogado()){
            mostrarTelaBarbeiroLogado()
        }
    }

    private fun verificaPermissoesLocalizacao() {
        if (!isPermissoesAceitas()){
            mostrarDialogIncentivoPermissaoLocalizacao()
        }else{
            configuraLocalizacao()
        }
    }

    @SuppressLint("MissingPermission")
    private fun configuraLocalizacao() {
        if (isPermissoesAceitas()){
            location.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 2.0f, this)
        }
    }

    private fun isPermissoesAceitas(): Boolean{
        return ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED  &&
                ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
    }

    private fun mostrarDialogIncentivoPermissaoLocalizacao() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(PERMISSION_INCENTIVE_MESAGE)
            .setCancelable(false)
            .setPositiveButton("Ok") { _: DialogInterface, _: Int ->
                mostrarPermissoes()
            }

        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun mostrarPermissoes() {
        ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION),LOCATION_REQUEST_CODE)
    }

    override fun onLocationChanged(location: Location) {
        currentLatlngUser =
            LatLng(lat = location.latitude,
            lng = location.longitude)
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    companion object{
        const val LOCATION_REQUEST_CODE = 2
        const val PERMISSION_INCENTIVE_MESAGE = "Para prosseguir, precisamos que aceite as permissões de localização, nós utilizamos ela para mostrar aos nossos usuários os barbeiros perto deles"
    }

}