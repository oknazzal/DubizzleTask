package com.odai.dubizzle.ui.details

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
abstract class DetailsActivityModule {

    @Binds
    abstract fun bindMainView(activity: DetailsActivity): DetailsContract.View

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun provideMainPresenter(view: DetailsContract.View): DetailsContract.Presenter {
            return DetailsPresenter(view)
        }
    }
}
