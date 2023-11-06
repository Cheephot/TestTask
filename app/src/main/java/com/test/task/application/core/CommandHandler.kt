package com.test.task.application.core

fun interface CommandHandler<COMMAND : Command<RESULT>, RESULT> {

    suspend fun handle(command: COMMAND): RESULT
}