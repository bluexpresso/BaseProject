package com.idslogic.levelshoes.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Color.parseColor
import android.graphics.drawable.PictureDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.text.toSpannable
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.di.GlideApp
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.JsonParser
import java.util.*

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

private fun isValidColorCode(color: String) = (color.startsWith("#") && color.length == 7)

fun parseColorFromString(color: String?): Int {
    return if (color.isNullOrEmpty() || !isValidColorCode(color))
        parseColor("#000000")
    else parseColor(color)
}

fun formatPrice(
    context: Context,
    currency: String,
    regularPrice: Double?,
    finalPrice: Double?
): Spannable {
    val spannableStringBuilder = SpannableStringBuilder()
    finalPrice?.let { price ->
        if (regularPrice != null && regularPrice > price) {
            spannableStringBuilder.append("$price $currency ")
            spannableStringBuilder.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.priceDiscounted
                    )
                ), 0, spannableStringBuilder.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableStringBuilder.append("$regularPrice $currency ")
            spannableStringBuilder.setSpan(
                StrikethroughSpan(),
                spannableStringBuilder.indexOf("$regularPrice"),
                spannableStringBuilder.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableStringBuilder.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.disabledColor)),
                spannableStringBuilder.indexOf("$regularPrice"),
                spannableStringBuilder.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
        } else {
            spannableStringBuilder.append("$finalPrice $currency")
        }
    }
    return spannableStringBuilder
}

fun String.strikeThrough() =
    SpannableString(this).apply {
        setSpan(StrikethroughSpan(), 0, this.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    }

fun String.foregroundColor(context: Context, @ColorRes color: Int): SpannableString {
    val formattedString = SpannableString(this)
    formattedString.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context, color)),
        0,
        this.length,
        Spannable.SPAN_INCLUSIVE_INCLUSIVE
    )
    return formattedString
}

fun SpannableStringBuilder.spansAppend(
    text: CharSequence,
    vararg spans: Any
): SpannableStringBuilder {
    val start = length
    append(text)

    spans.forEach { span ->
        setSpan(span, start, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    }

    return this
}