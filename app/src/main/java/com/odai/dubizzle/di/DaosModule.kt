package com.odai.dubizzle.di

import com.odai.dubizzle.data.daos.movie.LocalMovieDao
import com.odai.dubizzle.data.database.MyDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
@Module
class DaosModule {

    @Provides
    @Singleton
    fun provideUsersDao(database: MyDatabase): LocalMovieDao = database.usersDao
}
