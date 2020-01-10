package co.jeanpidev.comicvine.base

import androidx.lifecycle.ViewModel
import co.jeanpidev.comicvine.di.component.ViewModelInjector
import co.jeanpidev.comicvine.di.module.NetworkModule
import co.jeanpidev.comicvine.di.component.DaggerViewModelInjector
import co.jeanpidev.comicvine.ui.ComicDetailsViewModel
import co.jeanpidev.comicvine.ui.ComicListViewModel
import co.jeanpidev.comicvine.ui.ComicViewModel
import co.jeanpidev.comicvine.ui.DetailsViewModel

abstract class BaseViewModel : ViewModel() {
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is ComicListViewModel -> injector.inject(this)
            is ComicViewModel -> injector.inject(this)
            is ComicDetailsViewModel -> injector.inject(this)
            is DetailsViewModel -> injector.inject(this)
        }
    }
}
