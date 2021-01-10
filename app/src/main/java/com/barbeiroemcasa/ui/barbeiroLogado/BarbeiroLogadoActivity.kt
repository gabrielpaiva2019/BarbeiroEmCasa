package com.barbeiroemcasa.ui.barbeiroLogado

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.barbeiroemcasa.BaseActivity
import com.barbeiroemcasa.R
import com.barbeiroemcasa.model.LatLng
import com.barbeiroemcasa.ui.cadastro.CadastroBarbeiroActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.map_barbeiro_bottom_sheet.*


class BarbeiroLogadoActivity : BaseActivity(), LocationListener, OnMapReadyCallback {
    lateinit var location: LocationManager
    private var currentLatlngUser: LatLng? = null
    private lateinit var googleMap: GoogleMap
    lateinit var viewModel: BarbeiroLogadoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barbeiro_logado)

        inicializaVariaveis()
        inicializaMapa()
        inicializaBottomSheet()
    }

    private fun inicializaBottomSheet() {
        val bottomSheet = findViewById<View>(R.id.bottomSheetBarbeiroLogado)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        val buttonEntendi = findViewById<Button>(R.id.buttonEntendiBottomSheetMaps)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        buttonEntendi.setOnClickListener { bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED }
    }

    private fun inicializaMapa() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapaBarbeiro) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    private fun inicializaVariaveis() {
        viewModel = getViewModel(BarbeiroLogadoViewModel::class.java, application)
        location = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onLocationChanged(location: Location) {
        salvaLocalizacaoFirebase(location)
        googleMap.clear()
        configuraMarcador(location)
    }

    private fun mostrarDialogIncentivoNaoDesinstalar() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Olá, tudo bem? \n somos uma nova empresa e estamos com uma versao beta do nosso app, pedimos que nao desinstale por favor" +
                " estamos criando uma rede de usuarios e em breve você começará receber clientes atraves de nosso app :)")
            .setCancelable(false)
            .setPositiveButton("Ok") { _: DialogInterface, _: Int ->
            }

        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun salvaLocalizacaoFirebase(location: Location) {
         currentLatlngUser =
            LatLng(
                lat = location.latitude,
                lng = location.longitude
            )

        viewModel.salvarLocalizacaoBarbeiro(currentLatlngUser = currentLatlngUser!!)
    }

    override fun onStart() {
        super.onStart()
        configuraLocalizacao()
        mostrarDialogIncentivoNaoDesinstalar()
    }

    @SuppressLint("MissingPermission")
    private fun configuraLocalizacao() {
        location.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000, 2.0f, this)
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            this.googleMap = googleMap
        }
    }


    private fun configuraMarcador(location: Location) {
            viewModel.locationLiveData.observe(this, Observer {

                var position =
                    location?.latitude?.let {
                        location?.longitude?.let { it1 ->
                            com.google.android.gms.maps.model.LatLng(
                                it,
                                it1
                            )
                        }
                    }


                    googleMap?.addMarker(
                        position?.let {
                            MarkerOptions()
                                .position(it)
                                .title("Você está aqui :)")
                        }
                    )
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 32.0f))

                googleMap.animateCamera(CameraUpdateFactory.newLatLng(position))

        })

    }

    override fun onBackPressed() {

    }

}