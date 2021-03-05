package com.barbeiroemcasa.infra

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.barbeiroemcasa.ui.barbeiroLogado.BarbeiroLogadoViewModel
import com.barbeiroemcasa.ui.cadastro.CadastroBarbeiroViewModel
import com.barbeiroemcasa.ui.clientelogado.ClienteLogadoViewModel
import com.barbeiroemcasa.ui.feed.FeedViewModel
import com.barbeiroemcasa.ui.home.HomeViewModel
import com.barbeiroemcasa.ui.login.BarbeiroLoginViewModel
import com.barbeiroemcasa.ui.uploadimage.SubirImagemFeedViewModel

class ViewModelFactory(
    private val application: Application): ViewModelProvider.Factory  {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(application)
                isAssignableFrom(BarbeiroLoginViewModel::class.java) ->
                    BarbeiroLoginViewModel(application)
                isAssignableFrom(CadastroBarbeiroViewModel::class.java) ->
                    CadastroBarbeiroViewModel(application)
                isAssignableFrom(BarbeiroLogadoViewModel::class.java) ->
                    BarbeiroLogadoViewModel(application)
                isAssignableFrom(ClienteLogadoViewModel::class.java) ->
                    ClienteLogadoViewModel(application)
                isAssignableFrom(SubirImagemFeedViewModel::class.java) ->
                    SubirImagemFeedViewModel(application)
                isAssignableFrom(FeedViewModel::class.java) ->
                    FeedViewModel(application)
                else ->
                    throw ViewModelFactoryException()
            }
        } as T
}