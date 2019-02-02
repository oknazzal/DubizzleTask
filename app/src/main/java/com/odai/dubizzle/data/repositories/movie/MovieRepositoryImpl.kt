package com.odai.dubizzle.data.repositories.movie

import android.content.Context
import com.odai.dubizzle.data.daos.movie.LocalMovieDao
import com.odai.dubizzle.data.daos.movie.RemoteMovieDao
import com.odai.dubizzle.data.database.ITEMS_PER_PAGE
import com.odai.dubizzle.di.NetworkModule
import com.odai.dubizzle.models.Movie
import com.odai.dubizzle.utils.isNetworkConnected
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
class MovieRepositoryImpl(
    private val context: Context,
    private val localMovieDao: LocalMovieDao,
    private val remoteMovieDao: RemoteMovieDao
) : MovieRepository {

    override fun getMovies(page: Int, minYear: Number?, maxYear: Number?): Single<MutableList<Movie>> {
        return if (!context.isNetworkConnected()) {
            localMovieDao.getMovies(ITEMS_PER_PAGE * page)
        } else {
            Single.create<MutableList<Movie>> { emitter ->
                val query = mutableMapOf(
                    "page" to page,
                    "api_key" to NetworkModule.API_KEY,
                    "primary_release_date.gte" to minYear?.toInt(),
                    "primary_release_date.lte" to maxYear?.toInt()
                )
                remoteMovieDao.getMovies(query)
                    .subscribeOn(Schedulers.io())
                    .map { wrapper -> wrapper.data }
                    .flatMap { list ->
                        if (page == 1) {
                            localMovieDao.deleteAll()
                            localMovieDao.insert(list).toSingleDefault(list)
                        } else {
                            localMovieDao.insert(list).toSingleDefault(list)
                        }
                    }
                    .flatMap { localMovieDao.insert(it).toSingleDefault(it) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ emitter.onSuccess(it) }, { emitter.onError(it) })
            }
        }
    }
}
