package com.api

import kotlinx.coroutines.*
import java.io.*

object TideCommandExecutor {

    private const val loginCommand = "tide login"

    /**
     * Logs in to TIDE-CLI asynchronously.
     */
    fun login(callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = handleCommandLine(loginCommand.split(" "))
                withContext(Dispatchers.Main) {
                    callback(true) // Login success
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(false) // Login failed
                }
            }
        }
    }

    /**
     * Executes a command asynchronously.
     * @param command the command to execute.
     * @param workingDirectory optional working directory.
     * @return the results of the execution.
     */
    suspend fun handleCommandLine(command: List<String>, workingDirectory: File? = null): String =
        withContext(Dispatchers.IO) {
            val pb = ProcessBuilder(command)
            if (workingDirectory != null) {
                pb.directory(workingDirectory)
            }

            pb.redirectErrorStream(true)
            val process = pb.start()

            val output = process.inputStream.bufferedReader().use { it.readText() }

            val exitCode = process.waitFor()
            println("Process exited with code: $exitCode")

            if (exitCode != 0) {
                throw IOException("TIDE command failed with exit code $exitCode.\n$output")
            }

            output
        }
}
