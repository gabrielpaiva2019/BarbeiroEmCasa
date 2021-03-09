package com.barbeiroemcasa

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.barbeiroemcasa.infra.ViewModelFactory
import com.barbeiroemcasa.ui.loading.LoadingDialogFragment

open class BaseActivity: AppCompatActivity() {
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

    fun showLoading(){
        loadingDialogFragment?.let {
            if (!it.isVisible){
                it.show(supportFragmentManager, "TAG")
            }
        }
    }

}