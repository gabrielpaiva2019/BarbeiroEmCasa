package com.barbeiroemcasa.ui.feed

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.barbeiroemcasa.BaseActivity
import com.barbeiroemcasa.R
import com.barbeiroemcasa.model.LatLng
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : BaseActivity() {

    lateinit var viewModel: FeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        configViewModel()
        configObservers()

        viewModel.callListaFeed()
    }

    private fun configObservers() {
        viewModel.feedLiveData.observe(this, Observer {

            recyclerViewFeed.layoutManager = LinearLayoutManager(this)
            recyclerViewFeed.adapter = FeedAdapter(it)
        })
    }

    private fun configViewModel() {
        viewModel = getViewModel(FeedViewModel::class.java, application)
    }

}