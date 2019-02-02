package com.odai.dubizzle.data.daos.movie

import com.odai.dubizzle.models.BaseWrapper
import com.odai.dubizzle.models.Movie
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.QueryMap
import javax.inject.Inject

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
class RemoteMovieDao @Inject constructor(private val userService: UsersService) {

    interface UsersService {

        @POST("discover/movie")
        fun getMovies(@QueryMap query: Map<String, @JvmSuppressWildcards Any?>): Single<BaseWrapper<MutableList<Movie>>>
    }

    fun getMovies(query: Map<String, Any?>): Single<BaseWrapper<MutableList<Movie>>> {
        return userService.getMovies(query)
    }
}
