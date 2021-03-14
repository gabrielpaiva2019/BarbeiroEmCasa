package com.barbeiroemcasa.ui.uploadimage

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.barbeiroemcasa.infra.ApplicationSession
import com.barbeiroemcasa.model.Feed
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*

class SubirImagemFeedViewModel(application: Application) :
    AndroidViewModel(application), LifecycleObserver {

    val feedSalvo = MutableLiveData<Boolean>().apply { value = false }
    val error = MutableLiveData<String>()


    fun saveFeedItem(uriImageUpload: Uri?, feed: Feed) {
        ApplicationSession.barbeiro?.let {
            feed.whatsappBarbeiro = it.whatsappBarbeiro
            feed.dataPublicacao = getDateFormated()
            feed.horaPublicacao = getHourFormated()
            feed.nomeBarbeiro = it.nomeBarbeiro
            feed.uidBarbeiro = FirebaseAuth.getInstance().uid!!

            saveImageFirebaseStorage(uriImageUpload, feed)
        }
    }

    private fun saveImageFirebaseStorage(uriImageUpload: Uri?, feed: Feed) {
        val randomUid = UUID.randomUUID().toString()
        feed.uidFeed = randomUid

        val storage = FirebaseStorage.getInstance().getReference("feed/barbeiro/$randomUid")
        storage.putFile(uriImageUpload!!).addOnSuccessListener {
            storage.downloadUrl.addOnSuccessListener {
                feed.urlFoto = it.toString()
                saveDataFirebaseDatabase(feed)
            }.addOnFailureListener {
                error.value = it.localizedMessage
            }
        }
    }

    private fun saveDataFirebaseDatabase(feed: Feed) {
        val randomUid = UUID.randomUUID().toString()
        val uidbarbeiro = FirebaseAuth.getInstance().uid

        val firebaseDatabase =
            FirebaseDatabase.getInstance().getReference("feed/barbeiros/$randomUid")
        val firebaseDatabaseBarbeiro =
            FirebaseDatabase.getInstance().getReference("usuarios/barbeiros/$uidbarbeiro/feedsPublicados/$randomUid")

        firebaseDatabase.setValue(feed).addOnSuccessListener {

        }.addOnFailureListener {
            error.value = it.localizedMessage
        }
        firebaseDatabaseBarbeiro.setValue(feed).addOnSuccessListener {
            feedSalvo.value = true
        }.addOnFailureListener {
            error.value = it.localizedMessage
        }
    }

    private fun getHourFormated(): String {
        val locale = Locale("pt", "BR")
        return SimpleDateFormat("hh:mm", locale).format(Date())
    }

    private fun getDateFormated(): String {
        val locale = Locale("pt", "BR")

        var dayFormated = SimpleDateFormat("dd", locale).format(Date())
        var monthFormated = SimpleDateFormat("MMMM", locale).format(Date())

        return "dia $dayFormated de $monthFormated"

    }
}