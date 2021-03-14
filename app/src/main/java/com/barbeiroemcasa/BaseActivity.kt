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
import com.barbeiroemcasa.util.AnalyticsUtil

open class BaseActivity() :  AppCompatActivity() {
    private var loadingDialogFragment: LoadingDialogFragment? = null

    fun <T: ViewModel> getViewModel(viewModelClass: Class<T>, application: Application): T {
        val factory = ViewModelFactory(application)
        return ViewModelProviders.of(this, factory).get(viewModelClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AnalyticsUtil.track(this, "ENTROU_NA_ACTIVITY")

    }

    internal fun hideLoading(){
        loadingDialogFragment?.let {
            if (it.isVisible){
                supportFragmentManager.beginTransaction().remove(it).commit()
                it.dismiss()
                supportFragmentManager.executePendingTransactions()
            }
        }
    }

    private val onLoadingDialogCancelListener = object : LoadingDialogFragment.OnLoadingDialogCancelListener{
        override fun onCancel() {
            loadingDialogFragment?.dismiss()
        }

    }

    fun showLoading(title: String = DEFAULT_TITLE){
        supportFragmentManager?.let {
            loadingDialogFragment = LoadingDialogFragment.newInstance(it, onLoadingDialogCancelListener)
            it.beginTransaction().setCustomAnimations(R.anim.animation_fragment_enter, R.anim.animation_fragment_exit)
        }

        loadingDialogFragment?.let {
            if (!it.isVisible){
                it.setCustomTitle(title)
                it.show(supportFragmentManager, "TAG")
            }
        }
    }

}