package co.jeanpidev.comicvine.ui

import androidx.lifecycle.MutableLiveData
import co.jeanpidev.comicvine.base.BaseViewModel
import co.jeanpidev.comicvine.model.Issue
import java.text.SimpleDateFormat
import java.util.*

class ComicViewModel : BaseViewModel() {
    private val originalDateFormat = "yyyy-MM-dd HH:mm:ss"
    private val targetDateFormat = "MMMM dd, yyyy"
    private val comicName = MutableLiveData<String>()
    private val comicDate = MutableLiveData<String>()
    private val comicImage = MutableLiveData<String>()

    fun bind(comic: Issue) {
        comicName.value = comic.volume.name + " #" + comic.issue_number
        val originalFormat = SimpleDateFormat(originalDateFormat, Locale.getDefault())
        val targetFormat = SimpleDateFormat(targetDateFormat, Locale.getDefault())
        val date = originalFormat.parse(comic.date_added)
        comicDate.value = targetFormat.format(date)
        comicImage.value = comic.image.original_url
    }

    fun getComicName(): MutableLiveData<String> {
        return comicName
    }

    fun getComicDate(): MutableLiveData<String> {
        return comicDate
    }

    fun getComicImage(): MutableLiveData<String> {
        return comicImage
    }
}
