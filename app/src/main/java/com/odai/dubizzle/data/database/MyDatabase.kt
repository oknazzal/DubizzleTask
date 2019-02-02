package com.odai.dubizzle.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.odai.dubizzle.data.daos.movie.LocalMovieDao
import com.odai.dubizzle.models.Movie

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
@Database(
    entities = [
        Movie::class
    ], version = 1, exportSchema = true
)
abstract class MyDatabase : RoomDatabase() {

    abstract val usersDao: LocalMovieDao
}

const val ITEMS_PER_PAGE = 20