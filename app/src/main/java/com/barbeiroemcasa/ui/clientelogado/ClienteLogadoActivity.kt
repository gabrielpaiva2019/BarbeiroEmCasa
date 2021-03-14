package com.barbeiroemcasa.ui.clientelogado

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.barbeiroemcasa.BaseActivity
import com.barbeiroemcasa.R
import com.barbeiroemcasa.model.Barbeiro
import com.barbeiroemcasa.model.LatLng
import com.barbeiroemcasa.ui.cadastro.CadastroBarbeiroActivity
import com.barbeiroemcasa.ui.feed.FeedActivity
import com.barbeiroemcasa.util.AnalyticsUtil
import kotlinx.android.synthetic.main.activity_cliente_logado.*

class ClienteLogadoActivity : BaseActivity(), LocationListener {
    lateinit var viewModel: ClienteLogadoViewModel
    private lateinit var locationManager: LocationManager
    private var currentLatlngUser: LatLng? = null
    var currentSelectedKm = 7.0

    var recyclerJaFoiConfigurada = false


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_logado)

        iniciaVariaveis()
        iniciaListeners()

        viewModel.clienteActivityParaGambiarra = this

        if (isPermissoesAceitas()) {
            currentLatlngUser?.let { viewModel.queryBarbeirosAoRedor(it, currentSelectedKm) }
        }
    }

    fun showProgressBar(){
        recyclerViewBarbeirosDisponiveis.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar(){
        recyclerViewBarbeirosDisponiveis.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun iniciaListeners() {
        fabFeed.setOnClickListener {
            startActivity(Intent(this, FeedActivity::class.java))
            AnalyticsUtil.track(this, "cliente_logado")
        }

        toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isPermissoesAceitas()) {
                when (checkedId) {
                    R.id.buttonSevenKm -> {
                        currentSelectedKm = 7.0
                        recyclerJaFoiConfigurada = false
                        currentLatlngUser?.let { viewModel.queryBarbeirosAoRedor(it, 7.0) }

                    }
                    R.id.buton15km -> {
                        currentSelectedKm = 15.0
                        recyclerJaFoiConfigurada = false
                        currentLatlngUser?.let { viewModel.queryBarbeirosAoRedor(it, 15.0) }
                    }
                    R.id.button30km -> {
                        currentSelectedKm = 30.0
                        recyclerJaFoiConfigurada = false
                        currentLatlngUser?.let { viewModel.queryBarbeirosAoRedor(it, 30.0) }
                    }

                    R.id.button100km -> {
                        currentSelectedKm = 100.0
                        recyclerJaFoiConfigurada = false
                        currentLatlngUser?.let { viewModel.queryBarbeirosAoRedor(it, 100.0) }
                    }
                }

            } else {
                mostrarDialogIncentivoPermissaoLocalizacao()
            }
        }
    }


    private fun iniciaVariaveis() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        viewModel = getViewModel(ClienteLogadoViewModel::class.java, application)
    }

    override fun onStart() {
        super.onStart()
        verificaPermissoesLocalizacao()

    }

    private fun verificaPermissoesLocalizacao() {
        if (!isPermissoesAceitas()) {
            mostrarDialogIncentivoPermissaoLocalizacao()
        } else {
            configuraLocalizacao()
        }
    }

    @SuppressLint("MissingPermission")
    private fun configuraLocalizacao() {
        if (isPermissoesAceitas()) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                60000, 2.0f, this
            )
        }
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

    override fun onLocationChanged(location: Location) {
        currentLatlngUser =
            LatLng(
                lat = location.latitude,
                lng = location.longitude
            )

        currentLatlngUser?.let { viewModel.queryBarbeirosAoRedor(it, currentSelectedKm) }

        locationManager.removeUpdates(this)

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            configuraLocalizacao()
        }
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    fun setUpRecyclerView(listaBarbeiros: MutableList<Barbeiro>) {
        var listaBackup = mutableListOf<Barbeiro>()
        for (barbeiro in listaBarbeiros) {
            listaBackup.add(barbeiro)
        }

        recyclerViewBarbeirosDisponiveis.layoutManager = LinearLayoutManager(this)
        recyclerViewBarbeirosDisponiveis.adapter = ClienteLogadoAdapter(listaBackup)
        recyclerJaFoiConfigurada = true
    }

}