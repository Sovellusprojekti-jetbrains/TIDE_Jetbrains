package com.api

import com.actions.ActiveState
import com.actions.Settings
import com.course.SubTask
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.findDocument
import com.intellij.util.io.awaitExit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

object TideCommandExecutor {

    private const val loginCommand = "tide login"
    private const val logoutCommand = "tide logout"
    private const val coursesCommand = "tide courses --json"
    private const val checkLoginCommand = "tide check-login --json"
    private const val submitCommand = "tide submit"
    private const val taskCreateCommand = "tide task create"

    /**
     * Logs in to TIDE-CLI asynchronously.
     */
    fun login() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val output = handleCommandLine(loginCommand.split(" ")) // Run command
                withContext(Dispatchers.Main) {
                    val activeState = ActiveState.getInstance()
                    // TODO: This can't be the right way to do this.
                    if (output.contains("Login successful!") || output.contains("Logged in")) {
                        activeState.login()
                    } else {
                        activeState.logout()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val activeState = ActiveState.getInstance()
                    activeState.logout()
                }
            }
        }
    }

    fun fetchCoursesAsync() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val jsonString = handleCommandLine(coursesCommand.split(" ")) // Already runs in Dispatchers.IO
                val handler = JsonHandler()
                val courses = handler.jsonToCourses(jsonString)

                val activeState = ActiveState.getInstance()
                activeState.setCourses(courses)  // No need to switch dispatcher unless UI update is needed

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun checkLogin() {
        CoroutineScope(Dispatchers.IO).launch {
            val activeState = service<ActiveState>() // Get IntelliJ service

            runCatching {
                val jsonOutput = handleCommandLine(checkLoginCommand.split(" ")) // Run command
                val output = Gson().fromJson(jsonOutput, LoginOutput::class.java) // Parse JSON

                if (output.loggedIn != null) {
                    activeState.login()
                } else {
                    activeState.logout()
                }
            }.onFailure { ex ->
                ex.printStackTrace()
                activeState.logout()
            }
        }
    }

    data class LoginOutput(
        @SerializedName("logged_in") val loggedIn: String?
    )

    /**
     * Logs out of TIDE-CLI asynchronously.
     */
    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Doesn't really care if you're logged in or not.
                val result = handleCommandLine(logoutCommand.split(" "))
                withContext(Dispatchers.Main) {
                    val activeState = ActiveState.getInstance()
                    activeState.logout()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val activeState = ActiveState.getInstance()
                    activeState.login()
                }
            }
        }
    }


    /**
     * Syncs changes and submits the file to TIM.
     * @param file The file to submit
     */
    fun submitExercise(file: VirtualFile) {
        CoroutineScope(Dispatchers.IO).launch {
            syncChanges(file)
            val activeState = ActiveState.getInstance()
            try {
                val commandLineArgs: ArrayList<String> = ArrayList(submitCommand.split(" "))
                commandLineArgs.add(file.path)
                val response = handleCommandLine(commandLineArgs)
                activeState.setTideSubmitResponse(response)
            } catch (ex: Exception) {
                LogHandler.logError("121: TideCommandExecutor.submitExercise(VirtualFile file)", ex)
                LogHandler.logDebug(arrayOf("121 VirtualFile file"), arrayOf(file.toString()))
                ex.printStackTrace()
                // use tideBaseResponse to print the exception for the user
                activeState.setTideBaseResponse("Exception:" + System.lineSeparator() + ex)
            }
        }
    }


    /**
     * This method is used to save changes in virtual file to physical file on disk.
     * @param file Virtual file
     */
    private fun syncChanges(file: VirtualFile) {
        val fileDocumentManager: FileDocumentManager = FileDocumentManager.getInstance()
        CoroutineScope(Dispatchers.EDT).launch {
            val document: Document? = file.findDocument()
            if (document != null) {
                fileDocumentManager.saveDocument(document)
            }
        }
    }


    /**
     * Downloads an exercise from TIM into the location specified in plugin settings.
     * @param courseDir Course subdirectory to run TIDE-CLI in
     * @param cmdArgs Arguments for tide task create
     */
    fun loadExercise(courseDir: String, vararg cmdArgs: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val activeState = ActiveState.getInstance()
            if (cmdArgs.isEmpty()) {
                System.err.println("No arguments for tide create")
                LogHandler.logDebug(arrayOf("courseDir", "cmdArgs"), arrayOf(courseDir, ""))
                LogHandler.logInfo("loadExercise called without arguments")
            }
            val courseDirFile: File = File(Settings.getPath(), courseDir)
            if (!courseDirFile.exists()) {
                courseDirFile.mkdir()
            }

            val commandLineArgs: ArrayList<String> = ArrayList(taskCreateCommand.split(" "))
            commandLineArgs.addAll(cmdArgs)
            val response = handleCommandLine(commandLineArgs, courseDirFile)
            activeState.setTideBaseResponse(response)
        }
    }


    /**
     * Resets subtask back to the state of latest submit.
     * @param file Virtual file to get files local path and to communicate changes to idea's UI.
     * @param courseDir Course directory
     */
    fun resetSubTask(file: VirtualFile, courseDir: String) {
        val timData: String = Settings.getPath() + File.separatorChar + courseDir + File.separatorChar + ".timdata"
        val taskData: String = Files.readString(Path.of(timData), StandardCharsets.UTF_8)
        val handler: JsonHandler = JsonHandler();
        val subtasks: List<SubTask> = handler.jsonToSubtask(taskData)
        var taskId: String = ""
        var taskPath: String = ""
        var filePath = file.getPath()
        for (subtask: SubTask in subtasks) {
            for (name: String in subtask.fileName) {
                if (filePath.contains(name.replace("\"", ""))) {
                    taskId = subtask.ideTaskId
                    taskPath = subtask.path
                    break
                }
            }
            if (taskId == "") {
                break
            }
        }

        if (taskId != "") {
            syncChanges(file)
            loadExercise(courseDir, taskPath, taskId, "-f")
            // Virtual file must be refreshed and Intellij Idea's UI notified
            file.refresh(true, true)
            CoroutineScope(Dispatchers.IO).launch {
                if (file.isValid) {
                    file.parent.refresh(false, false)
                }
            }
        } else {
            com.views.InfoView.displayError("File open in editor is not a tide task!")
        }
    }


    /**
     * Opens a directory as a project in a new IDE instance.
     * @param taskPath Path to the directory to be opened as a project.
     */
    fun openTaskProject(taskPath: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var command: String = ""
            if (System.getenv("DEVELOP").equals("true")) {
                command = System.getenv("IDEA_LOCATION")
                LogHandler.logDebug(arrayOf("System.getenv(\"IDEA_LOCATION\")"),
                    arrayOf(System.getenv("IDEA_LOCATION")))
            } else {
                // TODO: This gets the directory where the IDE is installed.
                // Implement actual handling of supported IDEs and operating systems.
                command = PathManager.getHomePath()
            }

            // how does this behave in production?
            // do we need to catch and handle or print the string returned by handleCommandLine?
            handleCommandLine(listOf(command, taskPath))
        }
    }


    /**
     * Executes a command asynchronously.
     * @param command the command to execute.
     * @param workingDirectory optional working directory.
     * @return the results of the execution.
     */
    private suspend fun handleCommandLine(command: List<String>, workingDirectory: File? = null): String =
        withContext(Dispatchers.IO) {
            val pb = ProcessBuilder(command)
            if (workingDirectory != null) {
                pb.directory(workingDirectory)
            }

            pb.redirectErrorStream(true)
            val process = pb.start()

            val output = process.inputStream.bufferedReader().use { it.readText() }

            val exitCode = process.awaitExit()
            println("Process exited with code: $exitCode")

            if (exitCode != 0) {
                throw IOException("TIDE command failed with exit code $exitCode.\n$output")
            }

            output
        }
}
