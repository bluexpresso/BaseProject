package com.idslogic.levelshoes.data.models

data class ListingProductResponse(
    var meta: Meta?,
    var result: ArrayList<ListingProduct>?,
    var filters: ArrayList<Filter>?,
    var price: Price?,
    val popularProducts : ArrayList<ListingProduct>?,
)