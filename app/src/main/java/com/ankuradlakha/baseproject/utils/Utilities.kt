package com.ankuradlakha.baseproject.utils

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import com.ankuradlakha.baseproject.di.GlideApp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.gson.Gson
import com.google.gson.JsonParser

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

fun Glide.loadSVG(context: Context, uri: String) =
    GlideApp.with(context)
        .`as`(PictureDrawable::class.java)
        .transition(DrawableTransitionOptions.withCrossFade())
        .listener(SvgSoftwareLayerSetter())
        .load(Uri.parse(uri))

fun <T> formatJson(json: String, cls: Class<T>): T {
    val jsonObject = JsonParser.parseString(json)
    return Gson().fromJson(jsonObject, cls)
}
