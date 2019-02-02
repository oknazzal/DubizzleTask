package com.odai.dubizzle.data.repositories.movie

import com.odai.dubizzle.models.Movie
import io.reactivex.Single

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
interface MovieRepository {

    fun getMovies(page: Int, minYear: Number?, maxYear: Number?): Single<MutableList<Movie>>
}
