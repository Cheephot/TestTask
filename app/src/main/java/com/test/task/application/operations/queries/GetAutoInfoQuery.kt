package com.test.task.application.operations.queries

import com.test.task.application.core.Query
import com.test.task.application.core.QueryHandler
import com.test.task.dataStores.AutoInfoDataStore
import com.test.task.dataStores.models.AutoInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

object GetAutoInfoQuery : Query<AutoInfo>

interface GetAutoInfoQueryHandler : QueryHandler<GetAutoInfoQuery, AutoInfo>

class GetAutoInfoQueryHandlerImpl @Inject constructor(
    private val autoInfoDataStore: AutoInfoDataStore
) : GetAutoInfoQueryHandler {

    override fun handle(query: GetAutoInfoQuery): Flow<AutoInfo> {
        return autoInfoDataStore.data
    }
}