package com.odai.dubizzle.di

import com.bumptech.glide.RequestManager
import com.odai.dubizzle.binding.BindingAdapters
import com.odai.dubizzle.binding.BindingAdaptersImpl
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
class BindingModule {

    @Provides
    @Singleton
    fun provideTrutedBindingAdapters(glide: RequestManager): BindingAdapters {
        return BindingAdaptersImpl(glide)
    }
}
