package co.jeanpidev.comicvine.ui

import androidx.lifecycle.MutableLiveData
import co.jeanpidev.comicvine.base.BaseViewModel
import co.jeanpidev.comicvine.model.EntityDetail

class DetailsViewModel : BaseViewModel() {
    private val name = MutableLiveData<String>()
    private val image = MutableLiveData<String>()

    fun bind(detail: EntityDetail) {
        name.value = detail.name
        image.value = detail.image.original_url
    }

    fun getName(): MutableLiveData<String> {
        return name
    }

    fun getImage(): MutableLiveData<String> {
        return image
    }
}
