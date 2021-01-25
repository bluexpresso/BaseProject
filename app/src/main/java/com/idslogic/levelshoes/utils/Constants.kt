package com.idslogic.levelshoes.utils

const val GENDER_MEN = "mobileapp_landing_men"
const val GENDER_WOMEN = "mobileapp_landing_women"
const val GENDER_KIDS = "mobileapp_landing_kids"
const val LANGUAGE_ENGLISH = "en"
const val LANGUAGE_ARABIC = "ar"
const val DEFAULT_STARTUP_LANGUAGE = LANGUAGE_ENGLISH
const val CONTENT_TYPE_VIDEO = "video"
const val CONTENT_TYPE_IMAGE = "image"
const val CONTENT_TYPE_BUTTON = "button"
const val CONTENT_TYPE_TEXT = "text"
const val CONTENT_TYPE_HEADING = "heading"
const val CONTENT_TYPE_SUBHEADING = "subheading"
const val BOX_TYPE_SLIDER = "slider_view"
const val BOX_TYPE_REGISTER_SIGN_IN = "register_sign_in"
const val BOX_TYPE_BOX_VIEW = "box_view"
const val BOX_TYPE_PRODUCT_VIEW = "product_view"
const val BOX_TYPE_ADDITIONAL_PRODUCTS_VIEW = "additionalproduct_view"
const val GENDER_ID_MEN = 39
const val GENDER_ID_WOMEN = 61
const val GENDER_ID_KIDS = 1610
const val SHORT_ANIMATION_DURATION = 300L
const val MEDIUM_ANIMATION_DURATION = 500L
const val LONG_ANIMATION_DURATION = 750L
const val STATUS_ACTIVE = "active"
const val TYPE_IMAGE = "image"
const val TYPE_VIDEO = "video"
const val MAGNETO = "vue_storefront_magento"
const val DEFAULT_COUNTRY = "ae"
const val JSON_FIELD_HITS = "hits"
const val JSON_FIELD_SOURCE = "_source"
const val JSON_FIELD_OPTIONS = "options"
const val VIEW_ALL_COLLECTION = "View all collection"
const val IMAGE_URL = "https://staging-levelshoes-m2.vaimo.net/media/catalog/product"
fun getStoreCode(country: String?, selectedLanguage: String) =
    MAGNETO.plus("_").plus(country ?: DEFAULT_COUNTRY).plus("_").plus(selectedLanguage)

val productFields = arrayOf(
    "name",
    "final_price",
    "regular_price",
    "media_gallery",
    "configurable_options",
    "thumbnail",
    "configurable_children",
    "size_options",
    "size",
    "description",
    "meta_description",
    "image",
    "manufacturer",
    "color",
    "configurable_children.sku",
    "configurable_options",
    "material",
    "media_gallery",
    "lvl_category",
    "lvl_concession_type",
    "sku",
    "stock",
    "country_of_manufacture",
    "id",
    "badge_name",
    "special_to_date",
    "lvl_non_purchasable",
    "special_from_date"
)