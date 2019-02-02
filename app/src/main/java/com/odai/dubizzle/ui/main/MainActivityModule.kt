package com.odai.dubizzle.ui.main

import com.odai.dubizzle.data.repositories.movie.MovieRepository
import com.odai.dubizzle.ui.main.MainContract.View
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
@Suppress("unused")
@Module
abstract class MainActivityModule {

    @Binds
    abstract fun bindMainView(activity: MainActivity): View

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun provideMainPresenter(view: View, movieRepository: MovieRepository): MainContract.Presenter {
            return MainPresenter(view, movieRepository)
        }
    }
}
