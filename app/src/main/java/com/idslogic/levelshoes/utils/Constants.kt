package com.idslogic.levelshoes.utils

const val GENDER_MEN = "mobileapp_landing_men"
const val GENDER_WOMEN = "mobileapp_landing_women"
const val GENDER_KIDS = "mobileapp_landing_kids"
const val GENDER_BOYS = "mobileapp_landing_boys"
const val GENDER_GIRLS = "mobileapp_landing_girls"
const val GENDER_UNISEX = "mobileapp_landing_unisex"
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
const val TERM_VAL = "srinivas_ankur_android"
val GENDER_KIDS_ARRAY = hashMapOf(
    Pair("KIDS", 1610),
    Pair("BOYS", 88), Pair("GIRLS", 109), Pair("UNISEX", 1430)
)
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
const val CATEGORY_MEN = "Men"
const val CATEGORY_WOMEN = "Women"
const val CATEGORY_KIDS = "Kids"
const val CATEGORY_BOY = "Boy"
const val CATEGORY_GIRL = "Girl"
const val CATEGORY_UNISEX = "Unisex"
const val CATEGORY_BABY = "Baby"
const val GENDER_ID_MEN_SEARCH = 122
const val GENDER_ID_WOMEN_SEARCH = 3
const val GENDER_ID_KIDS_SEARCH = 209
const val DESIGNERS = "designers"
const val COLLECTIONS = "Collections"
const val VIEW_ALL_DESIGNERS_MEN_ID = "1671"
const val VIEW_ALL_DESIGNERS_WOMEN_ID = "1655"
const val VIEW_ALL_DESIGNERS_KID_ID = "1684"

const val MENU_ITEM_TYPE_LINK = "link"
const val MENU_ITEM_TYPE_DEFAULT = "default"
const val MENU_ITEM_TYPE_TITLE = "title"
const val NO_CATEGORY = -1
fun getStoreCode(country: String?, selectedLanguage: String) =
    MAGNETO.plus("_").plus(country ?: DEFAULT_COUNTRY).plus("_").plus(selectedLanguage)

val seasonDescList = arrayOf(1870, 2019)

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
    "manufacturer_label",
    "country_of_manufacture_label",
    "size_label",
    "color_label",
    "id",
    "badge_name",
    "special_to_date",
    "lvl_non_purchasable",
    "special_from_date"
)

const val FILTER_TYPE_CATEGORY = "lvl_category"
const val FILTER_TYPE_MANUFACTURER = "manufacturer"
const val FILTER_TYPE_COLOR = "color"
const val FILTER_TYPE_SIZE = "size"
const val FILTER_TYPE_GENDER = "gender"
const val FILTER_TYPE_VISIBILITY = "visibility"
const val FILTER_TYPE_SUBCATEGORY = "subcategory"
const val FILTER_TYPE_PRICE = "price"

val categorySearchFields = arrayOf(
    "name",
    "id",
    "menu_item_type",
    "column_breakpoint",
    "parent_id",
    "is_active",
    "include_in_menu",
    "menu_item_link",
    "children_data.name"
)
val gender_val = arrayOf(
    "Male",
    "Female"
)
val title_val = arrayOf(
    "Mr.",
    "Ms.",
    "Mrs."
)