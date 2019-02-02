package com.odai.dubizzle.data.daos.movie

import androidx.room.*
import com.odai.dubizzle.data.database.ITEMS_PER_PAGE
import com.odai.dubizzle.models.Movie
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
@Dao
interface LocalMovieDao {

    @Query("SELECT * FROM movies LIMIT $ITEMS_PER_PAGE OFFSET :skip")
    fun getMovies(skip: Int): Single<MutableList<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: MutableList<Movie>): Completable

    @Delete
    fun delete(movie: MutableList<Movie>): Completable

    @Query("DELETE FROM movies")
    fun deleteAll()
}
