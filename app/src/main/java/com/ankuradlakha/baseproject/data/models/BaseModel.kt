package com.ankuradlakha.baseproject.data.models

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
        lateinit var score: String

        @Expose
        @SerializedName("_source")
        var source: T? = null
    }
}