package org.agrosurvey.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.agrosurvey.PreferenceHelper
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object StorageHiltModules {

    @Provides
    @Singleton
    fun providePreferenceHelper(sp: SharedPreferences): PreferenceHelper {
        return PreferenceHelper(sp)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setPrettyPrinting().create()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context) =
        appContext.getSharedPreferences(
            appContext.packageName,
            Context.MODE_PRIVATE
        )

}