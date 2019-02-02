package com.odai.dubizzle.di

import com.odai.dubizzle.ui.details.DetailsActivity
import com.odai.dubizzle.ui.details.DetailsActivityModule
import com.odai.dubizzle.ui.main.MainActivity
import com.odai.dubizzle.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
@Suppress("unused")
@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [DetailsActivityModule::class])
    abstract fun bindDetailsActivity(): DetailsActivity
}
