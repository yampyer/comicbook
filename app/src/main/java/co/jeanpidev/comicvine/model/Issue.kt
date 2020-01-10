package co.jeanpidev.comicvine.model

data class Issue (
    val api_detail_url: String,
    val cover_date: String,
    val date_added: String,
    val date_last_updated: String,
    val description: String,
    val id: Long,
    val issue_number: String,
    val name: String,
    val store_date: String,
    val image: ImageData,
    val volume: Volume,
    val character_credits: ArrayList<Character>,
    val team_credits: ArrayList<Team>,
    val location_credits: ArrayList<Location>
)
