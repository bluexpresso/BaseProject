package com.idslogic.levelshoes.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color.parseColor
import android.graphics.drawable.PictureDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.custom.SortView
import com.idslogic.levelshoes.custom.SortView.Companion.SORT_BY_HIGHEST_PRICE
import com.idslogic.levelshoes.custom.SortView.Companion.SORT_BY_LOWEST_PRICE
import com.idslogic.levelshoes.custom.SortView.Companion.SORT_BY_NEWEST_FIRST
import com.idslogic.levelshoes.custom.SortView.Companion.SORT_BY_RELEVANCE
import com.idslogic.levelshoes.data.models.CategorySearch
import com.idslogic.levelshoes.di.GlideApp
import java.io.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
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
    val symbols = DecimalFormatSymbols(Locale.ENGLISH)
    symbols.decimalSeparator = '.'
    symbols.groupingSeparator = ','
    val decimalFormat = DecimalFormat("###,###.##", symbols)
    finalPrice?.let { price ->
        if (regularPrice != null && regularPrice > price) {
            val priceFormatted = decimalFormat.format(price)
            val regularPriceFormatted = decimalFormat.format(regularPrice)
            spannableStringBuilder.append("$priceFormatted $currency ")
            spannableStringBuilder.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.priceDiscounted
                    )
                ), 0, spannableStringBuilder.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableStringBuilder.append("$regularPriceFormatted $currency ")
            spannableStringBuilder.setSpan(
                StrikethroughSpan(),
                spannableStringBuilder.indexOf(regularPriceFormatted),
                spannableStringBuilder.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableStringBuilder.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.disabledColor)),
                spannableStringBuilder.indexOf(regularPriceFormatted),
                spannableStringBuilder.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
        } else {
            spannableStringBuilder.append("${decimalFormat.format(finalPrice)} $currency")
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
    imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    editText?.requestFocus()
}

fun showSoftKeyboard(view: View?) {
    if (view?.requestFocus() == true) {
        val imm =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun hideSoftKeyboard(view: View?) {
    view?.clearFocus()
    val imm =
        view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.RESULT_HIDDEN)
}

fun forceHideKeyboard(view: View?) {
    val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.hideSoftInputFromWindow(view?.windowToken, 0)
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

fun getGenderIdFromLink(link: String): String? {
    if (link.contains("|")) {
        val split = link.split("|")
        if (split.size == 2)
            return split[1]
    }
    return null
}

fun getCategoryIdFromLink(link: String): Int? {
    if (link.contains("|")) {
        val split = link.split("|")
        if (split.size == 2)
            return split[0].toInt()
    }
    return null
}

fun getGenderFilter(gender: String?, language: String): Pair<String, String> {
    when (gender) {
        GENDER_MEN -> {
            return if (language == LANGUAGE_ARABIC) Pair("gender:للرجال", "للرجال")
            else Pair("gender:Men", "Men")
        }
        GENDER_WOMEN -> {
            return if (language == LANGUAGE_ARABIC) Pair("للنساء:gender", "للنساء")
            else Pair("gender:Women", "Women")
        }
        GENDER_KIDS -> {
            return if (language == LANGUAGE_ARABIC) Pair(
                "gender:للأطفال الرضع;;gender:للجنسين;;gender:للبنات",
                "للأطفال"
            )
            else
                Pair(
                    "gender:${CATEGORY_BABY.toLowerCase(Locale.getDefault())};;gender:${
                        CATEGORY_GIRL
                    };;gender:${
                        CATEGORY_BOY
                    };;gender:${
                        CATEGORY_UNISEX
                    }", "Kids"
                )
        }
        else -> {
            return Pair("gender:Women", "Women")
        }
    }
}

fun getCategoryToPLPNavigation(
    navController: NavController,
    navigationId: Int,
    gender: String?,
    parentCategory: CategorySearch?,
    categorySearch: CategorySearch?
) {
    navController.navigate(navigationId, Bundle().apply {
        val categoryId: Int
        var genderFilter = ""
        val sbCategoryPath = java.lang.StringBuilder()
        if (isDesignerCategory(parentCategory?.name)) {
            sbCategoryPath.append(DESIGNERS)
            if (!isViewAllCategory(categorySearch?.name)) {
                sbCategoryPath.append(";${categorySearch?.name?.toLowerCase(Locale.getDefault())}")
            }
        } else if (isCollectionsCategory(parentCategory?.name)) {
            sbCategoryPath.append(COLLECTIONS)
            if (!isViewAllCategory(categorySearch?.name)) {
                sbCategoryPath.append(";${categorySearch?.name?.toLowerCase(Locale.getDefault())}")
            }
        } else if (gender == GENDER_KIDS) {
            if (parentCategory?.name == CATEGORY_BABY || parentCategory?.name == CATEGORY_GIRL
                || parentCategory?.name == CATEGORY_UNISEX || parentCategory?.name == CATEGORY_BOY
            ) {
                //KIDS->BABY->SNEAKERS
                sbCategoryPath.append("$CATEGORY_KIDS;")
                    .append(
                        "${
                            parentCategory.name?.toLowerCase(Locale.getDefault())
                                ?.toLowerCase(Locale.getDefault())
                        }"
                    )
                if (!isViewAllCategory(categorySearch?.name)) {
                    sbCategoryPath.append(
                        ";${
                            categorySearch?.name?.toLowerCase(
                                Locale.getDefault()
                            )
                        }"
                    )
                }
            } else {
                sbCategoryPath.append("$CATEGORY_KIDS;")
                    .append(parentCategory?.name?.toLowerCase(Locale.getDefault()))
                if (!isViewAllCategory(categorySearch?.name)) {
                    sbCategoryPath.append(";${categorySearch?.name?.toLowerCase(Locale.getDefault())}")
                }
            }
        } else if (gender == GENDER_WOMEN || gender == GENDER_MEN) {
            sbCategoryPath.append(if (gender == GENDER_WOMEN) CATEGORY_WOMEN else CATEGORY_MEN)
            if (categorySearch?.id == null || isViewAllCategory(categorySearch?.name))
                sbCategoryPath.append(";${parentCategory?.name?.toLowerCase(Locale.getDefault())}")
            if (!isViewAllCategory(categorySearch?.name)) {
                sbCategoryPath.append(";${categorySearch?.name?.toLowerCase(Locale.getDefault())}")
            }
        }
        if (!categorySearch?.menuItemLink.isNullOrEmpty()) {
            categoryId =
                getCategoryIdFromLink(categorySearch?.menuItemLink!!) ?: NO_CATEGORY
            genderFilter = getGenderIdFromLink(categorySearch.menuItemLink!!) ?: ""
        } else {
            categoryId =
                if (isDesignerCategory(parentCategory?.name) || isCollectionsCategory(
                        parentCategory?.name
                    )
                ) {
                    when (gender) {
                        GENDER_WOMEN -> GENDER_ID_WOMEN_SEARCH
                        GENDER_MEN -> GENDER_ID_MEN_SEARCH
                        else -> categorySearch?.id ?: NO_CATEGORY
                    }
                } else if (!isViewAllCategory(categorySearch?.name)) {
                    categorySearch?.id ?: NO_CATEGORY
                } else {
                    parentCategory?.id ?: NO_CATEGORY
                }
        }
        val parentCategoryId: Int = parentCategory?.id ?: NO_CATEGORY
        putString(
            ARG_GENDER,
            if (parentCategory?.name == CATEGORY_BABY || parentCategory?.name == CATEGORY_GIRL
                || parentCategory?.name == CATEGORY_UNISEX || parentCategory?.name == CATEGORY_BOY
            ) parentCategory.name ?: "" else gender ?: GENDER_WOMEN
        )
        putString(ARG_GENDER_FILTER, genderFilter)
        putString(ARG_TITLE, categorySearch?.name)
        putString(ARG_CATEGORY_PATH, sbCategoryPath.toString())
        putInt(ARG_CATEGORY_ID, categoryId)
        putInt(ARG_PARENT_CATEGORY_ID, parentCategoryId)
    })
}

fun hideSoftInput(activity: Activity?, view: View?) {
    (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        view?.windowToken,
        0
    )
}

fun getCurrency(language: String, countryCode: String): String {
    when (countryCode) {
        "ae" -> {
            return if (language == LANGUAGE_ENGLISH)
                "AED"
            else
                "د.إ."
        }
        "sa" -> {
            return if (language == LANGUAGE_ENGLISH)
                "SAR"
            else
                "ر.س."
        }
        "kw" -> {
            return if (language == LANGUAGE_ENGLISH)
                "KWD"
            else
                "د.ك."
        }
        "om" -> {
            return if (language == LANGUAGE_ENGLISH)
                "OMR"
            else
                "ر.ع."
        }
        "bh" -> {
            return if (language == LANGUAGE_ENGLISH)
                "BHD"
            else
                "د.ب."
        }
        else -> return if (language == LANGUAGE_ENGLISH)
            "AED"
        else
            "د.إ."
    }
//    "AED" = "د.إ.";
//    "SAR" = "ر.س.";
//    "KWT" = "د.ك.";
//    "KWD" = "د.ك.";
//    "OMR" = "ر.ع.";
//    "BHD" = "د.ب."
}

fun getSortByValue(sortBy: String) =
    when (sortBy) {
        SORT_BY_HIGHEST_PRICE -> "desc"
        SORT_BY_LOWEST_PRICE -> "asc"
        SORT_BY_NEWEST_FIRST -> "newest_first"
        SORT_BY_RELEVANCE -> "rel"
        else -> null
    }

const val SCROLL_ROTATION_MAGNITUDE = 0.25f

/** The magnitude of rotation while the list is over-scrolled. */
const val OVERSCROLL_ROTATION_MAGNITUDE = -10

/** The magnitude of translation distance while the list is over-scrolled. */
const val OVERSCROLL_TRANSLATION_MAGNITUDE = 0.2f

/** The magnitude of translation distance when the list reaches the edge on fling. */
const val FLING_TRANSLATION_MAGNITUDE = 0.5f
inline fun <reified T : RecyclerView.ViewHolder> RecyclerView.forEachVisibleHolder(
    action: (T) -> Unit
) {
    for (i in 0 until childCount) {
        action(getChildViewHolder(getChildAt(i)) as T)
    }
}
//["ticket":"klevu-158358783414411589",
//                     "term":searchText,
//                     "paginationStartsFrom":0,
//                     "noOfResults":12,
//                     "showOutOfStockProducts":false,
//                     "fetchMinMaxPrice":true,
//                     "enableMultiSelectFilters":true,
//                     "sortOrder":"rel",
//                     "enableFilters":true,
//                     "applyResults":"",
//                     "visibility":"search",
//                     "category":"KLEVU_PRODUCT",
//                     "klevu_filterLimit":50,
//                     "sv":2219,
//                     "lsqt":"",
//                     "responseType":"json",
//                     "resultForZero":1,
//                     "gender":StrGen,
//
//            ] as [String : Any]
