package co.jeanpidev.comicvine.di.component

import co.jeanpidev.comicvine.di.module.NetworkModule
import co.jeanpidev.comicvine.ui.ComicDetailsViewModel
import co.jeanpidev.comicvine.ui.ComicListViewModel
import co.jeanpidev.comicvine.ui.ComicViewModel
import co.jeanpidev.comicvine.ui.DetailsViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for viewModels.
 */
@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {

    fun inject(comicListViewModel: ComicListViewModel)

    fun inject(comicViewModel: ComicViewModel)

    fun inject(comicDetailsViewModel: ComicDetailsViewModel)

    fun inject(detailsViewModel: DetailsViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}
