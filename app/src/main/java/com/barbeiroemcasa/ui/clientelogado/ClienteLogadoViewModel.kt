package com.barbeiroemcasa.ui.clientelogado

import android.app.Application
import androidx.lifecycle.*
import com.barbeiroemcasa.model.Barbeiro
import com.barbeiroemcasa.model.LatLng
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryEventListener
import com.google.firebase.database.*

class ClienteLogadoViewModel(application: Application):
    AndroidViewModel(application), LifecycleObserver {

    val loginSuccessLiveData = MutableLiveData<Boolean>().apply { value = false }

    val listaBarbeirosLiveData = MutableLiveData<List<Barbeiro>>()

    val listaBarbeiros = mutableListOf<Barbeiro>()

    fun queryBarbeirosAoRedor(currentLatlngUser: LatLng) {
        //### Getting providers
        val geoLocation = GeoLocation(currentLatlngUser.lat, currentLatlngUser.lng)
        val db : DatabaseReference = FirebaseDatabase.getInstance().getReference("localizacao/barbeiros")
        val geofire = GeoFire(db)

        val geoQuery : GeoQuery = geofire!!.queryAtLocation(GeoLocation(currentLatlngUser.lat, currentLatlngUser.lng), 7.0)

        geoQuery.addGeoQueryEventListener(object : GeoQueryEventListener {
            override fun onKeyEntered(key: String, location: GeoLocation) {
                    pegarDadosBarbeiroFirebase(key)

            }

            override fun onKeyExited(key: String) {

            }

            override fun onKeyMoved(key: String, location: GeoLocation) {

            }

            override fun onGeoQueryReady() {

            }

            override fun onGeoQueryError(error: DatabaseError) {

            }
        })
    }

    private fun pegarDadosBarbeiroFirebase(key: String) {
        val firebaseDatabase = FirebaseDatabase.getInstance().getReference("usuarios/barbeiros/$key")

        firebaseDatabase.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                configuraListaDeBarbeiros(snapshot)
            }

        })
    }

    private fun configuraListaDeBarbeiros(snapshot: DataSnapshot) {
        validaSeJaFoiAdicionadoOBarbeiroNaLista(snapshot)
        listaBarbeirosLiveData.value = listaBarbeiros
    }

    private fun validaSeJaFoiAdicionadoOBarbeiroNaLista(snapshot: DataSnapshot) {
        val barbeiroSnapshot = snapshot.getValue(Barbeiro::class.java)

        try {
            for (barbeiro in listaBarbeiros){
                if (barbeiroSnapshot != null){
                    barbeiro?.let {
                        if (it.uid != barbeiroSnapshot.uid){
                            listaBarbeiros.add(barbeiroSnapshot)
                        }
                    }
                }
            }
        } catch (exception: KotlinNullPointerException){


        } catch (concurrentException: ConcurrentModificationException){

        }


        if (listaBarbeiros.size == 0){

            barbeiroSnapshot?.let {
                listaBarbeiros.add(barbeiroSnapshot)
            }

        }
    }


}