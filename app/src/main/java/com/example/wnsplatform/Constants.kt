package com.example.wnsplatform

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import java.security.cert.CertPathBuilderSpi

object Constants {

    const val NEWS_APP_ID:String = "76b68f644a9e4f5c94ce83031b5d9628"
    const val NEWS_BASE_URL:String = "https://newsapi.org/v2/"
    const val APP_ID:String = "ad5457c2d3cbe0ab45433b586787b92d"
    const val BASE_URL:String = "https://api.openweathermap.org/data/"
    const val METRIC_UNIT:String = "metric"

    fun isNetworkAvailable(context: Context):Boolean{
        val connectivityManager = context.
                getSystemService(Context.CONNECTIVITY_SERVICE)as
                ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network)
                ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)-> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)-> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)-> true
                else -> false
            }

        }
        else{
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }

    }
}