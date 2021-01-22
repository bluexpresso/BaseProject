package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Product(
    @Expose
    @SerializedName("image")
    var image: String?,
    @Expose
    @SerializedName("lvl_concession_type")
    var lvlConcessionType: Long?,
    @Expose
    @SerializedName("thumbnail")
    var thumbnail: String?,
    @Expose
    @SerializedName("color")
    var color: Int?,
    @Expose
    @SerializedName("lvl_category")
    var lvlCategory: Int?,
    @Expose
    @SerializedName("media_gallery")
    var mediaGallery: ArrayList<MediaGallery?>?,
    @Expose
    @SerializedName("description")
    var description: String?,
    @Expose
    @SerializedName("configuration_options")
    var configurableOptions: ArrayList<ConfigurableOption?>?,
    @Expose
    @SerializedName("badge_name")
    var badgeName: String?,
    @Expose
    @SerializedName("manufacturer")
    var manufacturer: Long?,
    @Expose
    @SerializedName("manufacturerName")
    var manufacturerName: String?,
    @Expose
    @SerializedName("size_options")
    var sizeOptions: ArrayList<Int>?,
    @Expose
    @SerializedName("meta_description")
    var metaDescription: String?,
    @Expose
    @SerializedName("regular_price")
    var regularPrice: Long?,
    @Expose
    @SerializedName("country_of_mamnufacture")
    var countryOfManufacture: String?,
    @Expose
    @SerializedName("material")
    var material: Long?,
    @Expose
    @SerializedName("name")
    var name: String?,
    @Expose
    @SerializedName("configurable_children")
    var configurableChildren: ArrayList<Product?>?,
    @Expose
    @SerializedName("special_from_date")
    var specialFromDate: Any?,
    @Expose
    @SerializedName("id")
    var id: Long?,
    @Expose
    @SerializedName("sku")
    var sku: String?,
    @Expose
    @SerializedName("stock")
    var stock: Stock?,
    @Expose
    @SerializedName("lvl_non_purchasable")
    var lvlNonPurchasable: Long?,
    @Expose
    @SerializedName("special_to_date")
    var specialToDate: Any?,
    @Expose
    @SerializedName("short_description")
    var shortDescription: String?,
    @Expose
    @SerializedName("hscode")
    var hsCode: Long?,
    @Expose
    @SerializedName("gender")
    var gender: Int?,
    @Expose
    @SerializedName("use_in_crosslinking")
    var useInCrosslinking: Int?,
    @Expose
    @SerializedName("usagespecificity")
    var usagesSpecificity: String?,
    @Expose
    @SerializedName("heelheight")
    var heelHeight: Int?,
    @Expose
    @SerializedName("styleoraclenumber")
    var styleOracleNumber: String?,
    @Expose
    @SerializedName("division")
    var division: Long?,
    @Expose
    @SerializedName("msrp_display_actual_price_type")
    var msrpDisplayActualPriceType: String?,
    @Expose
    @SerializedName("consignmentflag")
    var consignmentFlag: String?,
    @Expose
    @SerializedName("final_price")
    var finalPrice: Long?,
    @Expose
    @SerializedName("zone")
    var zone: Long?,
    @Expose
    @SerializedName("price")
    var price: String?,
    @Expose
    @SerializedName("seasondesc")
    var seasonDesc: Int?,
    @Expose
    @SerializedName("corename")
    var coreName: String?,
    @Expose
    @SerializedName("visibility")
    var visibility: Int?,
    @Expose
    @SerializedName("meta_title")
    var metaTitle: String?,
    @Expose
    @SerializedName("tax_class_id")
    var taxClassId: Int?,
    @Expose
    @SerializedName("concessiontype")
    var concessionType: Int?,
    @Expose
    @SerializedName("weight")
    var weight: Long?,
    @Expose
    @SerializedName("in_html_sitemap")
    var inHtmlSiteMap: Long?,
    @Expose
    @SerializedName("lvl_size_fit")
    var lvlSizeFit: String?,
    @Expose
    @SerializedName("url_key")
    var urlKey: String?,
    @Expose
    @SerializedName("in_xml_sitemap")
    var inXmlSiteMap: Int?,
    @Expose
    @SerializedName("rw_shoppingfeeds_skip_submit")
    var rwShoppingFeedsSkipSubmit: Int?,
    @Expose
    @SerializedName("primaryvpn")
    var primaryVPN: String?,
    @Expose
    @SerializedName("size")
    var size: Int?,
    @Expose
    @SerializedName("special_price")
    var specialPrice: Any?,
    @Expose
    @SerializedName("subcategory")
    var subCategory: Long?,
    @Expose
    @SerializedName("status")
    var status: Int?,
    @Expose
    @SerializedName("displayableImage")
    var displayableImage: String?
) {
    constructor(name: String?) : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        name,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
    )
}

data class MediaGallery(
    @Expose
    @SerializedName("image")
    var image: String?,
    @Expose
    @SerializedName("pos")
    var pos: Int?,
    @Expose
    @SerializedName("type")
    var type: String?,
    @Expose
    @SerializedName("lab")
    var lab: String?,
    @Expose
    @SerializedName("is_returnable")
    var isReturnable: Boolean?,
    @Expose
    @SerializedName("lvl_concession_core_sku")
    var lvlConcessionCoreSku: String?,
)

data class ConfigurableOption(
    @Expose
    @SerializedName("attribute_id")
    var attributeId: Long?,
    @Expose
    @SerializedName("label")
    var label: String?,
    @Expose
    @SerializedName("values")
    var values: ArrayList<ConfigurableOptionValue?>?,
    @Expose
    @SerializedName("attribute_code")
    var attributeCode: String?,
    @Expose
    @SerializedName("stock")
    var stock: Stock?,
)

data class ConfigurableOptionValue(
    @Expose
    @SerializedName("value_index")
    var valueIndex: String?,
    @Expose
    @SerializedName("label")
    var label: String?
)

data class Stock(
    @Expose
    @SerializedName("is_in_stock")
    var isInStock: Boolean?,
    @Expose
    @SerializedName("stock_status")
    var stockStatus: Int?,
    @Expose
    @SerializedName("min_qty")
    var minQty: Int?,
    @Expose
    @SerializedName("qty")
    var qty: Int?,
    @Expose
    @SerializedName("use_config_notify_stock_qty")
    var useConfigNotifyStockQty: Boolean?,
    @Expose
    @SerializedName("notify_stock_qty")
    var notifyStockQty: Int?,
)