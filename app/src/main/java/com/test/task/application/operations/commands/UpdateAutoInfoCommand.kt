package com.test.task.application.operations.commands

import com.test.task.application.core.Command
import com.test.task.application.core.CommandHandler
import com.test.task.dataStores.AutoInfoDataStore
import com.test.task.dataStores.models.AutoInfo
import javax.inject.Inject

data class UpdateAutoInfoCommand(val autoInfo: AutoInfo) : Command<Unit>

interface UpdateAutoInfoCommandHandler : CommandHandler<UpdateAutoInfoCommand, Unit>

class UpdateAutoInfoCommandHandlerImpl @Inject constructor(
    private val autoInfoDataStore: AutoInfoDataStore
) : UpdateAutoInfoCommandHandler {

    override suspend fun handle(command: UpdateAutoInfoCommand) {
        autoInfoDataStore.updateData { command.autoInfo }
    }
}