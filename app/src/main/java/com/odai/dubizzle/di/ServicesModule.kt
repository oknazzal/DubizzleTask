package com.odai.dubizzle.di

import com.odai.dubizzle.data.daos.movie.RemoteMovieDao
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
@Module
class ServicesModule {

    @Provides
    fun provideUserService(retrofit: Retrofit): RemoteMovieDao.UsersService {
        return retrofit.create(RemoteMovieDao.UsersService::class.java)
    }
}
