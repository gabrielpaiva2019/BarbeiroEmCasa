package com.barbeiroemcasa.ui.feed

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
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
import com.barbeiroemcasa.model.LatLng
import com.barbeiroemcasa.ui.cadastro.CadastroBarbeiroActivity
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : BaseActivity(), LocationListener {
    private lateinit var location: LocationManager
    private var currentLatlngUser: LatLng? = null

    lateinit var viewModel: FeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        location = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        configViewModel()
        configObservers()

    }

    override fun onStart() {
        super.onStart()
        verificaPermissoesLocalizacao()
        if (isPermissoesAceitas()) {


            currentLatlngUser?.let {
                viewModel.queryBarbeirosAoRedor(it)

            }
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

    @SuppressLint("MissingPermission")
    private fun configuraLocalizacao() {
        if (isPermissoesAceitas()) {
            location.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                60000, 2.0f, this
            )
        }
    }

    private fun verificaPermissoesLocalizacao() {
        if (!isPermissoesAceitas()) {
            mostrarDialogIncentivoPermissaoLocalizacao()
        } else {
            configuraLocalizacao()
        }
    }

    private fun configObservers() {
        viewModel.feedLiveData.observe(this, Observer {

            recyclerViewFeed.layoutManager = LinearLayoutManager(this)
            recyclerViewFeed.adapter = FeedAdapter(it)
        })

        viewModel.isListFeedEmpty.observe(this, Observer {
            if (it) {
                textViewInfoListFeedEmpty.visibility = View.GONE
            } else {
                textViewInfoListFeedEmpty.visibility = View.VISIBLE
            }
        })
    }

    private fun configViewModel() {
        viewModel = getViewModel(FeedViewModel::class.java, application)
    }

    override fun onLocationChanged(location: Location) {
        currentLatlngUser =
            LatLng(
                lat = location.latitude,
                lng = location.longitude
            )

        currentLatlngUser?.let {

            viewModel.queryBarbeirosAoRedor(it)


        }
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}


}