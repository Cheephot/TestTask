package com.test.task.di

import com.test.task.application.operations.commands.UpdateAutoInfoCommandHandler
import com.test.task.application.operations.commands.UpdateAutoInfoCommandHandlerImpl
import com.test.task.application.operations.commands.UpdateDriverLicenseNumberCommandHandler
import com.test.task.application.operations.commands.UpdateDriverLicenseNumberCommandHandlerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CommandsModule {

    @Binds
    abstract fun bindUpdateAutoInfoCommandHandlerImpl(
        handler: UpdateAutoInfoCommandHandlerImpl
    ): UpdateAutoInfoCommandHandler

    @Binds
    abstract fun bindUpdateDriverLicenseNumberCommandHandlerImpl(
        handler: UpdateDriverLicenseNumberCommandHandlerImpl
    ): UpdateDriverLicenseNumberCommandHandler
}