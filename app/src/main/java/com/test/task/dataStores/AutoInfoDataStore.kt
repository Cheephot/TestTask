package com.test.task.dataStores

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import com.test.task.dataStores.models.AutoInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class AutoInfoDataStore(
    private val preferencesDataStore: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataStore<AutoInfo> {

    companion object {
        private val stateRegistrationPlateKey = stringPreferencesKey("key_state_registration_plate")
        private val certificateNumberKey = stringPreferencesKey("key_certificate_number")
    }

    private val mutex = Mutex()

    override val data: Flow<AutoInfo>
        get() = preferencesDataStore.data.map { preferences -> preferences.toAutoInfo() }

    override suspend fun updateData(transform: suspend (t: AutoInfo) -> AutoInfo): AutoInfo {
        return mutex.withLock {
            withContext(dispatcher) {
                val updatedPreferences = preferencesDataStore.updateData { preferences ->
                    val currentInfo = preferences.toAutoInfo()
                    transform(currentInfo).toPreferences()
                }

                updatedPreferences.toAutoInfo()
            }
        }
    }

    private fun Preferences.toAutoInfo(): AutoInfo {
        val stateRegistrationPlate = this[stateRegistrationPlateKey]
        val certificateNumber = this[certificateNumberKey]

        return when {
            stateRegistrationPlate == null && certificateNumber == null -> {
                AutoInfo()
            }

            else -> AutoInfo(
                stateRegistrationPlate = stateRegistrationPlate ?: "",
                certificateNumber = certificateNumber ?: "",
            )
        }
    }

    private fun AutoInfo.toPreferences(): Preferences {
        return mutablePreferencesOf().apply {
            this[stateRegistrationPlateKey] = this@toPreferences.stateRegistrationPlate
            this[certificateNumberKey] = this@toPreferences.certificateNumber
        }
    }
}