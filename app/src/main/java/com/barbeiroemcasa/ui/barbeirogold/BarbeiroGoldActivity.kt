package com.barbeiroemcasa.ui.barbeirogold

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.billingclient.api.*
import com.barbeiroemcasa.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BarbeiroGoldActivity : AppCompatActivity() {


    lateinit var purchasesUpdatedListener: PurchasesUpdatedListener

    lateinit var billingClient: BillingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barbeiro_gold)


        purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->
               Toast.makeText(this, "dSADSA", Toast.LENGTH_SHORT).show()
            }

        billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    querySkuDetails()
                }
            }

            override fun onBillingServiceDisconnected() {
            }


        })

    }

    private fun querySkuDetails() {
        val skuList = ArrayList<String>()
        skuList.add("barbeiro_gold")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->

            val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetailsList!![0])
                .build()
            val responseCode = billingClient.launchBillingFlow(this, flowParams)


        }
    }
}