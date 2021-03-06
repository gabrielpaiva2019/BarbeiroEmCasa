package com.barbeiroemcasa.ui.feed

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.barbeiroemcasa.model.Feed
import com.barbeiroemcasa.model.LatLng
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryEventListener
import com.google.firebase.database.*

class FeedViewModel(application: Application) :
    AndroidViewModel(application), LifecycleObserver {

    var isListFeedEmpty = MutableLiveData<Boolean>()
    var feedLiveData = MutableLiveData<MutableList<Feed>>()
    var listFeed = mutableListOf<Feed>()
    val arrayKey = mutableListOf<String>()



    fun queryBarbeirosAoRedor(currentLatlngUser: LatLng) {
        //### Getting providers
        val geoLocation = GeoLocation(currentLatlngUser.lat, currentLatlngUser.lng)
        val db: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("localizacao/barbeiros")
        val geofire = GeoFire(db)

        val geoQuery: GeoQuery = geofire!!.queryAtLocation(
            GeoLocation(currentLatlngUser.lat, currentLatlngUser.lng),
            7.0
        )

        geoQuery.addGeoQueryEventListener(object : GeoQueryEventListener {
            override fun onKeyEntered(key: String, location: GeoLocation) {
                var length: Int

                length = arrayKey.size

                arrayKey.add(key)

                pegarDadosFeed(key)

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

    private fun pegarDadosFeed(key: String) {
        val firebaseDatabase = FirebaseDatabase.getInstance().getReference("feed/barbeiros/")

        firebaseDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshotFeed in snapshot.children) {
                    val feed = snapshotFeed.getValue(Feed::class.java)
                    if (feed?.uidBarbeiro == key) {
                        listFeed.add(feed)
                    }
                }

                feedLiveData.value = listFeed
                if (feedLiveData.value?.size == 0){
                    isListFeedEmpty.value = true
                }


            }

        })
    }


}