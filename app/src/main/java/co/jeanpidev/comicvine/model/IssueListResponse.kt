package co.jeanpidev.comicvine.model

import com.google.gson.annotations.SerializedName

data class IssueListResponse (
    val limit: Long,
    val offset: Long,
    val number_of_page_results: Long,
    val number_of_total_results: Long,
    val status_code: Int,
    @SerializedName("results")
    val issues: ArrayList<Issue>
)
