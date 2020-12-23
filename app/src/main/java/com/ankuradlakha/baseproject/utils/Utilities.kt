package com.ankuradlakha.baseproject.utils

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.di.GlideApp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

fun loadSVG(context: Context, uri: String) =
    GlideApp.with(context)
        .`as`(PictureDrawable::class.java)
        .transition(DrawableTransitionOptions.withCrossFade())
        .listener(SvgSoftwareLayerSetter())
        .load(uri)

fun <T> formatJson(json: String, cls: Class<T>): T {
    val jsonObject = JsonParser.parseString(json)
    return Gson().fromJson(jsonObject, cls)
}

fun getNoInternetDialog(context: Context) =
    MaterialAlertDialogBuilder(context, R.style.DialogTheme).setTitle(R.string.no_internet)
        .setCancelable(false)
        .setMessage(R.string.no_internet_message)
