package com.idslogic.levelshoes.ui.home.search

import com.idslogic.levelshoes.data.models.ListingProduct

data class SearchRecentTrendingWrapper(
    var product: ListingProduct?,
    var isHeader: Boolean,
    var title: String?
)