package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BaseModel<T> {
    @Expose
    @SerializedName("took")
    var took: Int = 0

    @Expose
    @SerializedName("timed_out")
    var timedOut: Boolean = false

    @Expose
    @SerializedName("_shards")
    lateinit var shards: Shards

    @Expose
    @SerializedName("hits")
    lateinit var hits: Hits<T>

    @Expose
    @SerializedName("aggregations")
    var aggregations: Aggregation? = null

    class Aggregation {
        @SerializedName("max_price")
        @Expose
        var maxPrice: Value? = null

        @SerializedName("min_price")
        @Expose
        var minPrice: Value? = null
    }

    class Value {
        @SerializedName("value")
        @Expose
        var value: Double? = null
    }

    class Shards {
        @Expose
        @SerializedName("total")
        var total: Int = 0

        @Expose
        @SerializedName("successful")
        var successful: Int = 0

        @Expose
        @SerializedName("skipped")
        var skipped: Int = 0

        @Expose
        @SerializedName("failed")
        var failed: Int = 0
    }

    class Hits<T> {
        @Expose
        @SerializedName("total")
        var total: Int = 0

        @Expose
        @SerializedName("max_score")
        lateinit var maxScore: String

        @Expose
        @SerializedName("hits")
        lateinit var hits: ArrayList<Hit<T>>
    }

    class Hit<T> {
        @Expose
        @SerializedName("_index")
        lateinit var index: String

        @Expose
        @SerializedName("type")
        lateinit var type: String

        @Expose
        @SerializedName("_id")
        lateinit var id: String

        @Expose
        @SerializedName("_score")
        var score: Double? = null

        @Expose
        @SerializedName("_source")
        var source: T? = null
    }
}