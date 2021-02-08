package com.idslogic.levelshoes.utils

import android.content.Context
import android.graphics.Color.parseColor
import android.graphics.drawable.PictureDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.UnderlineSpan
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.di.GlideApp
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
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

fun formatTabFonts(
    context: Context,
    tabLayout: TabLayout,
    selectedPosition: Int,
    noOfTabs: Int
) {
    for (j in 0..noOfTabs) {
        if (j == selectedPosition) {
            tabLayout.getTabAt(j)?.view?.children?.find { it is TextView }
                ?.let { tv ->
                    (tv as TextView).post {
                        tv.typeface = ResourcesCompat.getFont(context, R.font.bold)
                    }
                }
        } else {
            tabLayout.getTabAt(j)?.view?.children?.find { it is TextView }
                ?.let { tv ->
                    (tv as TextView).post {
                        tv.typeface =
                            ResourcesCompat.getFont(context, R.font.regular)
                    }
                }
        }
    }
}

fun String.underlined(): SpannableString {
    val spannableString = SpannableString(this)
    spannableString.setSpan(
        UnderlineSpan(),
        0,
        spannableString.length,
        Spannable.SPAN_INCLUSIVE_INCLUSIVE
    )
    return spannableString
}

fun getCategoryPathForSearch(gender: String?, category: String?, subCategory: String?): String {
    val sb = StringBuilder()
    sb.append(
        "${
            when (gender) {
                GENDER_WOMEN -> {
                    CATEGORY_WOMEN
                }
                GENDER_MEN -> {
                    CATEGORY_MEN
                }
                GENDER_KIDS -> {
                    CATEGORY_KIDS
                }
                GENDER_BOYS -> {
                    CATEGORY_BOY
                }
                GENDER_GIRLS -> {
                    CATEGORY_GIRL
                }
                GENDER_UNISEX -> {
                    CATEGORY_UNISEX
                }
                else -> gender
            }
        };${category?.toLowerCase(Locale.ENGLISH)}"
    )
    if (subCategory != null) sb.append(";${subCategory.toLowerCase(Locale.ENGLISH)}")
    return sb.toString()
}

fun openSoftInput(context: Context, editText: EditText?) {
    val imm: InputMethodManager? =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
}

fun <T : Serializable> deepCopy(obj: T?): T? {
    if (obj == null) return null
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(obj)
    oos.close()
    val bais = ByteArrayInputStream(baos.toByteArray())
    val ois = ObjectInputStream(bais)
    @Suppress("unchecked_cast")
    return ois.readObject() as T
}

fun isViewAllCategory(categoryName: String?): Boolean {
    return (categoryName?.contains("view", true) == true && categoryName.contains(
        "all",
        true
    )) || (categoryName?.contains("عرض", true) == true &&
            categoryName.contains("عرض", true))
}

fun isDesignerCategory(categoryName: String?): Boolean {
    return categoryName?.contains("designer", true) == true ||
            categoryName?.contains(" العلامة", true) == true
}

fun isCollectionsCategory(categoryName: String?): Boolean {
    return categoryName?.contains("collections", true) == true
            || categoryName?.contains(" التشكيلات", true) == true
}
