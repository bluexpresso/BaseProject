package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ListingProduct(
    @Expose
    @SerializedName("color")
    var color: String?,
    @Expose
    @SerializedName("gender")
    var gender: String?,
    @Expose
    @SerializedName("lvl_category")
    var lvlCategory: String?,
    @Expose
    @SerializedName("quantity_and_stock_status")
    var quantityAndStockStatus: String?,
    @Expose
    @SerializedName("created_at")
    var createdAt: String?,
    @Expose
    @SerializedName("discount")
    var discount: String?,
    @Expose
    @SerializedName("color_swatches")
    var colorSwatches: String?,
    @Expose
    @SerializedName("hideGroupPrices")
    var hideGroupPrices: String?,
    @Expose
    @SerializedName("itemGroupId")
    var itemGroupId: String?,
    @Expose
    @SerializedName("lcManufacturer")
    var lcManufacturer: String?,
    @Expose
    @SerializedName("manufacturer")
    var manufacturer: String?,
    @Expose
    @SerializedName("division")
    var divison: String?,
    @Expose
    @SerializedName("updated_at")
    var udpatedAt: String?,
    @Expose
    @SerializedName("freeShipping")
    var freeShipping: String?,
    @Expose
    @SerializedName("storeBaseCurrency")
    var storeBaseCurrency: String?,
    @Expose
    @SerializedName("zone")
    var zone: String?,
    @Expose
    @SerializedName("price")
    var price: String?,
    @Expose
    @SerializedName("seasondesc")
    var seasondesc: String?,
    @Expose
    @SerializedName("toPrice")
    var toPrice: String?,
    @Expose
    @SerializedName("imageUrl")
    var imageUrl: String?,
    @Expose
    @SerializedName("inStock")
    var inStock: String?,
    @Expose
    @SerializedName("currency")
    var currency: String?,
    @Expose
    @SerializedName("id")
    var id: String?,
    @Expose
    @SerializedName("imageHover")
    var imageHover: String?,
    @Expose
    @SerializedName("sku")
    var sku: String?,
    @Expose
    @SerializedName("startPrice")
    var startPrice: String?,
    @Expose
    @SerializedName("image")
    var image: String?,
    @Expose
    @SerializedName("deliveryInfo")
    var deliveryInfo: String?,
    @Expose
    @SerializedName("hideAddToCart")
    var hideAddToCart: String?,
    @Expose
    @SerializedName("salePrice")
    var salePrice: String?,
    @Expose
    @SerializedName("oldPrice")
    var oldPrice: String?,
    @Expose
    @SerializedName("weight")
    var weight: String?,
    @Expose
    @SerializedName("klevu_category")
    var klevuCategory: String?,
    @Expose
    @SerializedName("totalVariants")
    var totalVariants: String?,
    @Expose
    @SerializedName("groupPrices")
    var groupPrices: String?,
    @Expose
    @SerializedName("url")
    var url: String?,
    @Expose
    @SerializedName("primaryvpn")
    var primaryVpn: String?,
    @Expose
    @SerializedName("size")
    var size: String?,
    @Expose
    @SerializedName("name")
    var name: String?,
    @Expose
    @SerializedName("shortDesc")
    var shortDesc: String?,
    @Expose
    @SerializedName("customVisibility")
    var customVisibility: String?,
    @Expose
    @SerializedName("subCategory")
    var subCaegory: String?,
    @Expose
    @SerializedName("category")
    var category: String?,
    @Expose
    @SerializedName("typeOfRecord")
    var typeOfRecord: String?,
    @Expose
    @SerializedName("swatches")
    var swatches: Swatch?
)

data class Swatch(
    @Expose
    @SerializedName("lowestPrice")
    var lowestPrice: String,
    @Expose
    @SerializedName("numberOfAdditionalVariants")
    var numberOfAdditionalVariants: String
)