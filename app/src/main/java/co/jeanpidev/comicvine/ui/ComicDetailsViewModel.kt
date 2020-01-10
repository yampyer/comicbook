package co.jeanpidev.comicvine.ui

import android.view.View
import androidx.lifecycle.MutableLiveData
import co.jeanpidev.comicvine.R
import co.jeanpidev.comicvine.base.BaseViewModel
import co.jeanpidev.comicvine.model.*
import co.jeanpidev.comicvine.network.ComicApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ComicDetailsViewModel : BaseViewModel() {
    @Inject
    lateinit var comicApi: ComicApi

    //create a new Job
    private val parentJob = Job()
    //create a coroutine context with the job and the dispatcher
    private val coroutineContext : CoroutineContext get() = parentJob + Dispatchers.Default
    //create a coroutine scope with the coroutine context
    private val scope = CoroutineScope(coroutineContext)

    private var subscription: CompositeDisposable = CompositeDisposable()

    private val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    private val errorMessage: MutableLiveData<Int> = MutableLiveData()
    private val issueDetails: MutableLiveData<IssueResponse> = MutableLiveData()
    private val characters: MutableLiveData<ArrayList<Character>> = MutableLiveData()
    private val locations: MutableLiveData<ArrayList<Location>> = MutableLiveData()
    private val teams: MutableLiveData<ArrayList<Team>> = MutableLiveData()
    private val fieldListIssue = "api_detail_url,character_credits,cover_date,date_added,description,image,issue_number,location_credits,name,team_credits,volume"
    private val fieldListDetails = "image,name"
    private val unableResolveHost = "Unable to resolve host \"comicvine.gamespot.com\""

    init {
        loadingVisibility.value = View.GONE
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun getIssueDetails(url: String) {
        subscription.add(comicApi.getIssueDetails(url, fieldListIssue)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveDetailsStart() }
                .doOnTerminate { onRetrieveDetailsFinish() }
                .subscribe(this::onRetrieveDetailsSuccess, this::onRetrieveDetailsError))
    }

    private fun onRetrieveDetailsStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrieveDetailsFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveDetailsSuccess(details: IssueResponse) {
        issueDetails.value = details

        if (details.issue.character_credits.isNotEmpty()) {
            scope.launch {
                details.issue.character_credits.map { character ->
                    async {
                        character.detail = comicApi.getDetailsAsync(character.api_detail_url, fieldListDetails).detail
                    }
                }.awaitAll()

                withContext(Dispatchers.Main) {
                    characters.value = details.issue.character_credits
                }
            }
        }

        if (details.issue.team_credits.isNotEmpty()) {
            scope.launch {
                details.issue.team_credits.map { team ->
                    async {
                        team.detail = comicApi.getDetailsAsync(team.api_detail_url, fieldListDetails).detail
                    }
                }.awaitAll()

                withContext(Dispatchers.Main) {
                    teams.value = details.issue.team_credits
                }
            }
        }

        if (details.issue.location_credits.isNotEmpty()) {
            scope.launch {
                details.issue.location_credits.map { location ->
                    async {
                        location.detail = comicApi.getDetailsAsync(location.api_detail_url, fieldListDetails).detail
                    }
                }.awaitAll()

                withContext(Dispatchers.Main) {
                    locations.value = details.issue.location_credits
                }
            }
        }
    }

    private fun onRetrieveDetailsError(error: Throwable) {
        errorMessage.value = if (error.message!!.contains(unableResolveHost)) R.string.no_internet_connection else R.string.error_has_ocurred
    }

    fun getIssueDetails(): MutableLiveData<IssueResponse> = issueDetails

    fun getCharacters(): MutableLiveData<ArrayList<Character>> = characters

    fun getLocations(): MutableLiveData<ArrayList<Location>> = locations

    fun getTeams(): MutableLiveData<ArrayList<Team>> = teams

    fun getErrorMessage(): MutableLiveData<Int> = errorMessage

    fun getLoadingVisibility(): MutableLiveData<Int> = loadingVisibility
}
