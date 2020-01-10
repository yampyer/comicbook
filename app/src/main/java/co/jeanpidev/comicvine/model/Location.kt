package co.jeanpidev.comicvine.model

data class Location (
    val api_detail_url: String,
    val id: Long,
    val name: String,
    val site_detail_url: String,
    var detail: EntityDetail
)
