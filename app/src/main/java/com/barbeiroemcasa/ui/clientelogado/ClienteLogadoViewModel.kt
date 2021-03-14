package com.barbeiroemcasa.ui.clientelogado

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.barbeiroemcasa.model.Barbeiro
import com.barbeiroemcasa.model.LatLng
import com.barbeiroemcasa.service.Retrofit
import com.barbeiroemcasa.service.RetrofitService
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryEventListener
import com.google.firebase.database.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ConcurrentModificationException

class ClienteLogadoViewModel(application: Application) :
    AndroidViewModel(application), LifecycleObserver {

    lateinit var clienteActivityParaGambiarra: ClienteLogadoActivity

    fun queryBarbeirosAoRedor(currentLatlngUser: LatLng, km: Double) {
        clienteActivityParaGambiarra.showProgressBar()
        val retrofitClient = Retrofit
            .getBarbeiroServiceInstance()

        val endpoint = retrofitClient.create(RetrofitService::class.java)
        val callback =
            endpoint.getAroundBarbers(currentLatlngUser.lat, currentLatlngUser.lng, km)

        callback.enqueue(object : Callback<List<Barbeiro>> {
            override fun onFailure(call: Call<List<Barbeiro>>, t: Throwable) {
                clienteActivityParaGambiarra.hideProgressBar()
            }

            override fun onResponse(
                call: Call<List<Barbeiro>>,
                response: Response<List<Barbeiro>>
            ) {
                clienteActivityParaGambiarra.hideProgressBar()
                clienteActivityParaGambiarra.setUpRecyclerView(response.body() as MutableList<Barbeiro>)
            }
        })
    }


}