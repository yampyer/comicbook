package co.jeanpidev.comicvine.ui

import android.view.View
import androidx.lifecycle.MutableLiveData
import co.jeanpidev.comicvine.R
import co.jeanpidev.comicvine.base.BaseViewModel
import co.jeanpidev.comicvine.model.IssueListResponse
import co.jeanpidev.comicvine.network.ComicApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import co.jeanpidev.comicvine.utils.events.SingleLiveEvent
import javax.inject.Inject

class ComicListViewModel : BaseViewModel() {
    @Inject
    lateinit var comicApi: ComicApi

    private val comicListAdapter: ComicListAdapter = ComicListAdapter()

    private var subscription: CompositeDisposable = CompositeDisposable()

    private val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    private val errorMessage: MutableLiveData<Int> = MutableLiveData()
    private val issuesList: MutableLiveData<IssueListResponse> = MutableLiveData()
    private var onClickListTrigger = SingleLiveEvent<Unit>()
    private var onClickGridTrigger = SingleLiveEvent<Unit>()
    private val limit = 100L
    private val sort = "date_added:desc"
    private val unableResolveHost = "Unable to resolve host \"comicvine.gamespot.com\""

    init {
        loadingVisibility.value = View.GONE
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun getLatestIssues(offset: Long) {
        subscription.add(comicApi.getLatestIssues(offset, limit, sort)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveLatestIssuesStart() }
                .doOnTerminate { onRetrieveLatestIssuesFinish() }
                .subscribe(this::onRetrieveLatestIssuesSuccess, this::onRetrieveLatestIssuesError))
    }

    private fun onRetrieveLatestIssuesStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrieveLatestIssuesFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveLatestIssuesSuccess(latest: IssueListResponse) {
        issuesList.value = latest
        if (latest.issues.isNotEmpty()) {
            comicListAdapter.updateComicList(latest.issues)
        } else
            errorMessage.value = R.string.no_issues
    }

    private fun onRetrieveLatestIssuesError(error: Throwable) {
        errorMessage.value = if (error.message!!.contains(unableResolveHost)) R.string.no_internet_connection else R.string.error_has_ocurred
    }

    fun getErrorMessage(): MutableLiveData<Int> = errorMessage

    fun getLoadingVisibility(): MutableLiveData<Int> = loadingVisibility

    fun getComicListAdapter(): ComicListAdapter = comicListAdapter

    fun getOnClickListTrigger(): SingleLiveEvent<Unit> = onClickListTrigger

    fun getOnClickGridTrigger(): SingleLiveEvent<Unit> = onClickGridTrigger

    fun onClickListEvent() {
        onClickListTrigger.call()
    }

    fun onClickGridEvent() {
        onClickGridTrigger.call()
    }
}
