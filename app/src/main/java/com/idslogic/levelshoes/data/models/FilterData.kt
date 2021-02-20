package com.idslogic.levelshoes.data.models

data class FilterData(
    var sortBy: String?
) {
    constructor() : this(null)
}