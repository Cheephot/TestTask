package com.test.task.di

import com.test.task.application.operations.queries.GetAutoInfoQueryHandler
import com.test.task.application.operations.queries.GetAutoInfoQueryHandlerImpl
import com.test.task.application.operations.queries.GetDriverLicenseNumberQueryHandler
import com.test.task.application.operations.queries.GetDriverLicenseNumberQueryHandlerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class QueriesModule {

    @Binds
    abstract fun bindGetAutoInfoQueryHandlerImpl(
        handler: GetAutoInfoQueryHandlerImpl
    ): GetAutoInfoQueryHandler

    @Binds
    abstract fun bindGetDriveLicenseNumberQueryHandlerImpl(
        handler: GetDriverLicenseNumberQueryHandlerImpl
    ): GetDriverLicenseNumberQueryHandler
}