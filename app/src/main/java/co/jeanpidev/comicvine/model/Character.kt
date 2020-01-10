package co.jeanpidev.comicvine.model

data class Character (
    val api_detail_url: String,
    val id: Long,
    val name: String,
    val site_detail_url: String,
    var detail: EntityDetail
)
