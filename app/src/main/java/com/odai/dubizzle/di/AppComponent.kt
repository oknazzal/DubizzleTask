package com.odai.dubizzle.di

import androidx.databinding.DataBindingComponent
import com.odai.dubizzle.binding.BindingAdapters
import com.odai.dubizzle.common.MyApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuilderModule::class,
        AppModule::class,
        CommonModule::class,
        ServicesModule::class,
        NetworkModule::class,
        DaosModule::class,
        RepositoriesModule::class,
        BindingModule::class
    ]
)
interface AppComponent : AndroidInjector<MyApplication>, DataBindingComponent {

    override fun inject(instance: MyApplication)

    override fun getBindingAdapters(): BindingAdapters

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: MyApplication): Builder

        fun build(): AppComponent
    }
}
