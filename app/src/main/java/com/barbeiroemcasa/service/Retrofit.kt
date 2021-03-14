package com.barbeiroemcasa.service

import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.X509TrustManager


class Retrofit {
    companion object {
        fun getBarbeiroServiceInstance() : Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://barbeiro-em-casa.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private fun createOkHttpClient(): OkHttpClient {
            val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                )
                .build()

            return OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .build()
        }

        fun getOkHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .addInterceptor { chain ->
                    val finalToken =  "Bearer "+"live_13f614d5eb1d69462452d74ed226ec"
                   val request = chain.request().newBuilder()
                        .addHeader("Authorization",finalToken)
                        .build()
                    chain.proceed(request)
                }

            return  builder.build()
        }
    }

    class MyManager : X509TrustManager {

        override fun checkServerTrusted(
            p0: Array<out java.security.cert.X509Certificate>?,
            p1: String?
        ) {
            //allow all
        }

        override fun checkClientTrusted(
            p0: Array<out java.security.cert.X509Certificate>?,
            p1: String?
        ) {
            //allow all
        }

        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
            return arrayOf()
        }
    }
}