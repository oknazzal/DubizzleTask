package com.odai.dubizzle.di

import android.content.Context
import com.odai.dubizzle.common.MyApplication
import dagger.Binds
import dagger.Module

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
@Suppress("unused")
@Module
internal abstract class AppModule {

    @Binds
    abstract fun provideContext(application: MyApplication): Context
}
