package com.odai.dubizzle.di

import android.content.Context
import com.odai.dubizzle.data.daos.movie.LocalMovieDao
import com.odai.dubizzle.data.daos.movie.RemoteMovieDao
import com.odai.dubizzle.data.repositories.movie.MovieRepository
import com.odai.dubizzle.data.repositories.movie.MovieRepositoryImpl
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
class RepositoriesModule {

    @Singleton
    @Provides
    fun provideUserRepository(
        context: Context,
        localMovieDao: LocalMovieDao,
        remoteMovieDao: RemoteMovieDao
    ): MovieRepository {
        return MovieRepositoryImpl(context, localMovieDao, remoteMovieDao)
    }
}
