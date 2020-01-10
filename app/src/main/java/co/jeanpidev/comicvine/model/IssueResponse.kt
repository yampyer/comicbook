package co.jeanpidev.comicvine.model

import com.google.gson.annotations.SerializedName

data class IssueResponse (
    val status_code: Int,
    @SerializedName("results")
    val issue: Issue
)
