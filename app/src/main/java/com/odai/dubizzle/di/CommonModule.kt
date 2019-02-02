package com.odai.dubizzle.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.room.Room
import com.bumptech.glide.RequestManager
import com.odai.dubizzle.common.GlideApp
import com.odai.dubizzle.constants.Constants
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
class CommonModule {

    @Singleton
    @Provides
    fun provideGlideRequests(context: Context): RequestManager {
        return GlideApp.with(context)
    }

    @Singleton
    @Provides
    fun provideDatabase(context: Context): MyDatabase {
        return Room.databaseBuilder(
            context,
            MyDatabase::class.java,
            Constants.Database.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideNetworkRequest(): NetworkRequest {
        return NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
    }

    @Singleton
    @Provides
    fun provideConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}
