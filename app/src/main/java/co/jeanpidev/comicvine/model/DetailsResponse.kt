package co.jeanpidev.comicvine.model

import com.google.gson.annotations.SerializedName

data class DetailsResponse (
    val status_code: Int,
    @SerializedName("results")
    val detail: EntityDetail
)
