package com.idslogic.levelshoes.data.models

data class FilterData(
    var sortBy: String?,
    var categories: HashMap<String, FilterOption> = hashMapOf(),
    var genders: HashMap<String, FilterOption> = hashMapOf(),
    var colors: HashMap<String, FilterOption> = hashMapOf(),
    var manufacturers: HashMap<String, FilterOption> = hashMapOf(),
    var sizes: HashMap<String, FilterOption> = hashMapOf(),
    var price: HashMap<String, FilterOption> = hashMapOf(),
    var filteredPrice: Pair<String, String>?
) {
    constructor() : this(
        null,
        hashMapOf<String, FilterOption>(),
        hashMapOf<String, FilterOption>(),
        hashMapOf<String, FilterOption>(),
        hashMapOf<String, FilterOption>(),
        hashMapOf<String, FilterOption>(),
        hashMapOf<String, FilterOption>(),
        null
    )
}