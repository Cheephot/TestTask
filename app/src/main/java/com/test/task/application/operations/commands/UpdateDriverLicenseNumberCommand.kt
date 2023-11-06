package com.test.task.application.operations.commands

import com.test.task.application.core.Command
import com.test.task.application.core.CommandHandler
import com.test.task.application.formatters.LatinWithCyrillicFormatter
import com.test.task.dataStores.DriverLicenseNumberDataStore
import javax.inject.Inject

data class UpdateDriverLicenseNumberCommand(val driverLicenseNumber: String) : Command<Unit>

interface UpdateDriverLicenseNumberCommandHandler :
    CommandHandler<UpdateDriverLicenseNumberCommand, Unit>

class UpdateDriverLicenseNumberCommandHandlerImpl @Inject constructor(
    private val driverLicenseNumberDataStore: DriverLicenseNumberDataStore,
) : UpdateDriverLicenseNumberCommandHandler {

    override suspend fun handle(command: UpdateDriverLicenseNumberCommand) {
        val formattedDriverLicenseNumber = LatinWithCyrillicFormatter
            .replaceLatinWithCyrillic(command.driverLicenseNumber)
            .uppercase()
            .trim()

        driverLicenseNumberDataStore.updateData { formattedDriverLicenseNumber }
    }
}