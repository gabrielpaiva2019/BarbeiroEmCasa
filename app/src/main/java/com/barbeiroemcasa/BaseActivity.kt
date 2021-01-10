package com.barbeiroemcasa

import android.app.Application
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.barbeiroemcasa.infra.ViewModelFactory

open class BaseActivity: AppCompatActivity() {


    fun <T: ViewModel> getViewModel(viewModelClass: Class<T>, application: Application): T {
        val factory = ViewModelFactory(application)
        return ViewModelProviders.of(this, factory).get(viewModelClass)
    }

}