package com.barbeiroemcasa.ui.barbeiroLogado

import android.app.Application
import android.os.Handler
import androidx.lifecycle.*
import com.barbeiroemcasa.infra.ApplicationSession
import com.barbeiroemcasa.model.LatLng
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BarbeiroLogadoViewModel(application: Application):
    AndroidViewModel(application), LifecycleObserver {
    val liveData = MutableLiveData<String>().apply { value = "" }
    val locationLiveData = MutableLiveData<LatLng>().also {  }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun load() {
        Handler().postDelayed({
            liveData.value = "teste"
        }, 5000)

    }

     fun salvarLocalizacaoBarbeiro(currentLatlngUser: LatLng) {
        val geoLocation = GeoLocation(currentLatlngUser.lat, currentLatlngUser.lng)
        val db : DatabaseReference = FirebaseDatabase.getInstance().getReference("localizacao/barbeiros")
        val geofire = GeoFire(db)
        geofire.setLocation(FirebaseAuth.getInstance().uid, geoLocation)
         locationLiveData.value = currentLatlngUser
    }

}