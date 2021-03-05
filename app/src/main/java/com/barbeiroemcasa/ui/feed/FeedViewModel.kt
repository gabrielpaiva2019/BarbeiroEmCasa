package com.barbeiroemcasa.ui.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.barbeiroemcasa.model.Feed
import com.barbeiroemcasa.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FeedViewModel(application: Application) :
    AndroidViewModel(application), LifecycleObserver {

    var feedLiveData = MutableLiveData<MutableList<Feed>>()


    fun callListaFeed() {
        var firebaseDatabase = FirebaseDatabase.getInstance().getReference("feed/barbeiros")
        firebaseDatabase.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var listFeed = mutableListOf<Feed>()
                for (dataSnapshotFeed: DataSnapshot in snapshot.children) {
                    listFeed.add(dataSnapshotFeed.getValue(Feed::class.java)!!)
                    feedLiveData.value?.add(dataSnapshotFeed.getValue(Feed::class.java)!!)
                }

                feedLiveData.value = listFeed
            }

        })
    }


}