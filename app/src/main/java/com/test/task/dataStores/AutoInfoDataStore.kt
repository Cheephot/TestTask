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

data class AutoInfo(
    val stateRegistrationPlate: String = "",
    val certificateNumber: String = ""
)

class AutoInfoDataStore(private val preferencesDataStore: DataStore<Preferences>) :
    DataStore<AutoInfo> {

    companion object {
        val stateRegistrationPlateKey = stringPreferencesKey("key_state_registration_plate")
        val certificateNumberKey = stringPreferencesKey("key_certificate_number")
    }

    private val mutex = Mutex()

    override val data: Flow<AutoInfo>
        get() = preferencesDataStore.data.map {
            val stateRegistrationPlate = it[stateRegistrationPlateKey] ?: ""
            val certificateNumber = it[certificateNumberKey] ?: ""

            AutoInfo(
                stateRegistrationPlate = stateRegistrationPlate,
                certificateNumber = certificateNumber
            )
        }

    override suspend fun updateData(transform: suspend (t: AutoInfo) -> AutoInfo): AutoInfo {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                try {
                    val updateData = preferencesDataStore.updateData {
                        val stateRegistrationPlate = it[stateRegistrationPlateKey] ?: ""
                        val certificateNumber = it[certificateNumberKey] ?: ""

                        val autoInfo =
                            AutoInfo(
                                stateRegistrationPlate = stateRegistrationPlate,
                                certificateNumber = certificateNumber
                            )

                        preferencesOf(
                            stateRegistrationPlateKey to transform(autoInfo).stateRegistrationPlate,
                            certificateNumberKey to transform(autoInfo).certificateNumber
                        )
                    }

                    AutoInfo(
                        updateData[stateRegistrationPlateKey] ?: "",
                        updateData[certificateNumberKey] ?: ""
                    )
                } catch (ex: Exception) {
                    return@withContext data.firstOrNull() ?: AutoInfo()
                }
            }
        }
    }
}