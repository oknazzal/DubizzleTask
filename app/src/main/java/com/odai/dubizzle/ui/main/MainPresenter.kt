package com.odai.dubizzle.ui.main

import com.odai.dubizzle.data.network.HttpError
import com.odai.dubizzle.data.network.MySingleObserver
import com.odai.dubizzle.data.repositories.movie.MovieRepository
import com.odai.dubizzle.models.Movie
import com.odai.dubizzle.ui.base.BasePresenter
import com.odai.dubizzle.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
class MainPresenter(
    view: MainContract.View,
    private val movieRepository: MovieRepository
) : BasePresenter<MainContract.View>(view), MainContract.Presenter {

    override fun onViewInflated() {
        super.onViewInflated()
    }

    override fun getMovies(page: Int, minYear: Number?, maxYear: Number?) {
        compositeDisposable + movieRepository.getMovies(page, minYear, maxYear)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : MySingleObserver<MutableList<Movie>>() {
                override fun onSuccess(list: MutableList<Movie>) {
                    if (page == 1) {
                        view?.submitList(list)
                    } else {
                        view?.addList(list)
                    }
                }

                override fun onError(error: HttpError) {
                    super.onError(error)
                    view?.handleDataError(error)
                }
            })
    }
}
