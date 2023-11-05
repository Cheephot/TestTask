package com.test.task.dataStores

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class DriverLicenseNumberDataStore(private val preferencesDataStore: DataStore<Preferences>) :
    DataStore<String> {

    private val mutex = Mutex()

    companion object {
        val driverLicenseNumberKey = stringPreferencesKey("key_driver_license_number")
    }

    override val data: Flow<String>
        get() = preferencesDataStore.data.map {
            it[driverLicenseNumberKey] ?: ""
        }

    override suspend fun updateData(transform: suspend (t: String) -> String): String {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                try {
                    val updateData = preferencesDataStore.updateData {
                        preferencesOf(
                            driverLicenseNumberKey to transform(it[driverLicenseNumberKey] ?: "")
                        )
                    }

                    updateData[driverLicenseNumberKey] ?: ""
                } catch (ex: Exception) {
                    return@withContext data.firstOrNull() ?: ""
                }
            }
        }
    }
}
