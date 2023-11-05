package com.test.task.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.test.task.dataStores.AutoInfoDataStore
import com.test.task.dataStores.DriverLicenseNumberDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideAutoDataStore(@ApplicationContext context: Context): AutoInfoDataStore {
        return AutoInfoDataStore(
            preferencesDataStore = PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("auto_info") }
            )
        )
    }

    @Singleton
    @Provides
    fun provideDriverLicenseNumberDataStore(@ApplicationContext context: Context): DriverLicenseNumberDataStore {
        return DriverLicenseNumberDataStore(
            preferencesDataStore = PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("driver_license_number") }
            )
        )
    }
}