package com.barbeiroemcasa.ui.barbeiroLogado

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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.barbeiroemcasa.BaseActivity
import com.barbeiroemcasa.R
import com.barbeiroemcasa.infra.ApplicationSession
import com.barbeiroemcasa.model.LatLng
import com.barbeiroemcasa.ui.barbeirogold.BarbeiroGoldActivity
import com.barbeiroemcasa.ui.cadastro.CadastroBarbeiroActivity
import com.barbeiroemcasa.ui.feed.FeedActivity
import com.barbeiroemcasa.ui.home.HomeActivity
import com.barbeiroemcasa.ui.perfil.PerfilActivity
import com.barbeiroemcasa.ui.uploadimage.SubirImagemFeedActivity
import com.barbeiroemcasa.util.AnalyticsUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.tombayley.activitycircularreveal.CircularReveal
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
        val buttonSubirFoto = findViewById<CardView>(R.id.cardViewAdicionarFoto)
        val cardViewFeed = findViewById<CardView>(R.id.cardViewFeed)
        val cardViewSair = findViewById<CardView>(R.id.cardViewLogout)
        val cardViewPerfil = findViewById<CardView>(R.id.cardViewPerfil)
        val textViewBoasVindas = findViewById<TextView>(R.id.textViewBoasVindas)
        val cardViewBarbeiroGold = findViewById<CardView>(R.id.cardViewBarbeiroGold)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        cardViewBarbeiroGold.setOnClickListener {
//            startActivity(Intent(this, BarbeiroGoldActivity::class.java))
            Toast.makeText(this, "em breve ...", Toast.LENGTH_SHORT).show()
            AnalyticsUtil.track(this, "BARBEIRO_GOLD_EM_BREVE")
        }

        cardViewSair.setOnClickListener {
            AnalyticsUtil.track(this, "BARBEIRO_LOGOUT")
            FirebaseAuth.getInstance().signOut()
            this.finish()
        }
        buttonSubirFoto.setOnClickListener {
            startActivity(Intent(this, SubirImagemFeedActivity::class.java))
            AnalyticsUtil.track(this, "SUBIR_FOTO_FEED_BARBEIRO")
        }
        cardViewFeed.setOnClickListener {
            AnalyticsUtil.track(this, "FEED_ACTIVITY_BARBEIRO")
            startActivity(Intent(this, FeedActivity::class.java))
        }
        cardViewPerfil.setOnClickListener {
            AnalyticsUtil.track(this, "PERFIL_ACTIVITY_BARBEIRO")
            startActivity(Intent(this, PerfilActivity::class.java))
        }
    }

    private fun inicializaMapa() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapaBarbeiro) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
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

    private fun inicializaVariaveis() {
        viewModel = getViewModel(BarbeiroLogadoViewModel::class.java, application)
        location = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onLocationChanged(location: Location) {
        salvaLocalizacaoFirebase(location)
        googleMap.clear()
        configuraMarcador(location)
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
        if (ApplicationSession.barbeiro == null){
            FirebaseAuth.getInstance().signOut()
            this.finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }
        if (isPermissoesAceitas()) {
            configuraLocalizacao()
        } else {
            mostrarDialogIncentivoPermissaoLocalizacao()
        }

    }

    @SuppressLint("MissingPermission")
    private fun configuraLocalizacao() {
        location.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000, 2.0f, this
        )
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

}