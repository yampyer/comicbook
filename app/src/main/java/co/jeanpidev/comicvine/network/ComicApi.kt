package co.jeanpidev.comicvine.network

import co.jeanpidev.comicvine.model.DetailsResponse
import co.jeanpidev.comicvine.model.IssueListResponse
import co.jeanpidev.comicvine.model.IssueResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ComicApi {

    @GET("issues")
    fun getLatestIssues(@Query("offset") offset: Long, @Query("limit") limit: Long, @Query("sort") sort: String): Observable<IssueListResponse>

    @GET
    fun getIssueDetails(@Url url: String, @Query("field_list") fieldList: String): Observable<IssueResponse>

    @GET
    suspend fun getDetailsAsync(@Url url: String, @Query("field_list") fieldList: String): DetailsResponse
}
