package com.test.task.application.operations.queries

import com.test.task.application.core.Query
import com.test.task.application.core.QueryHandler
import com.test.task.dataStores.DriverLicenseNumberDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

object GetDriverLicenseNumberQuery : Query<String>

interface GetDriverLicenseNumberQueryHandler : QueryHandler<GetDriverLicenseNumberQuery, String>

class GetDriverLicenseNumberQueryHandlerImpl @Inject constructor(
    private val driverLicenseNumberDataStore: DriverLicenseNumberDataStore
) : GetDriverLicenseNumberQueryHandler {

    override fun handle(query: GetDriverLicenseNumberQuery): Flow<String> {
        return driverLicenseNumberDataStore.data
    }
}