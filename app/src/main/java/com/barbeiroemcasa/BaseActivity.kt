package com.barbeiroemcasa

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.barbeiroemcasa.infra.ViewModelFactory
import com.barbeiroemcasa.ui.loading.LoadingDialogFragment
import com.barbeiroemcasa.ui.loading.LoadingDialogFragment.Companion.DEFAULT_TITLE

open class BaseActivity() :  AppCompatActivity() {
    private var loadingDialogFragment: LoadingDialogFragment? = null

    fun <T: ViewModel> getViewModel(viewModelClass: Class<T>, application: Application): T {
        val factory = ViewModelFactory(application)
        return ViewModelProviders.of(this, factory).get(viewModelClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager?.let {
            loadingDialogFragment = LoadingDialogFragment.newInstance(it, onLoadingDialogCancelListener)
            it.beginTransaction().setCustomAnimations(R.anim.animation_fragment_enter, R.anim.animation_fragment_exit)
        }

    }

    internal fun hideLoading(){
        loadingDialogFragment?.let {
            if (it.isVisible){
                it.dismissAllowingStateLoss()
            }
        }
    }

    private val onLoadingDialogCancelListener = object : LoadingDialogFragment.OnLoadingDialogCancelListener{
        override fun onCancel() {
            onBackPressed()
        }

    }

    fun showLoading(title: String = DEFAULT_TITLE){
        loadingDialogFragment?.let {
            if (!it.isVisible){
                it.setCustomTitle(title)
                it.show(supportFragmentManager, "TAG")
            }
        }
    }

}