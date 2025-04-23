package com.api

import com.actions.Settings
import com.course.Course
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
import com.state.ActiveState
import com.views.InfoView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.Path


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
                    if (output.contains("Login successful!")) {
                        activeState.login()
                        // If no prior login details, don't show the "please finish logging in"-message.
                        InfoView.displayInfo("Login successful!")
                    } else if (output.contains("Logged in")) {
                        activeState.login()
                        InfoView.displayInfo(output)
                    } else {
                        activeState.logout()
                        InfoView.displayError(output)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val activeState = ActiveState.getInstance()
                    activeState.logout()
                    InfoView.displayError(e.message)
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
                for (crs: Course in courses) {
                    activeState.addDownloadedSubtasksToCourse(crs)
                }

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
                    InfoView.displayInfo(jsonOutput)
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
                    InfoView.displayInfo(result)
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
            activeState.updateCourses()
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
            for (taskFile: SubTask.TaskFile in subtask.taskFiles) {
                if (filePath.contains(taskFile.fileName)) {
                    taskId = subtask.ideTaskId
                    taskPath = subtask.path
                    break
                }
            }
            if (taskId != "") {
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
            if (System.getenv("DEVELOP") != null && System.getenv("DEVELOP").equals("true")) {
                command = System.getenv("IDEA_LOCATION")
                LogHandler.logDebug(arrayOf("System.getenv(\"IDEA_LOCATION\")"),
                    arrayOf(System.getenv("IDEA_LOCATION")))
            } else {
                // TODO: This gets the directory where the IDE is installed.
                // Implement actual handling of supported IDEs and operating systems.
                command = PathManager.getHomePath() + "/bin/"
                val appInfo = ApplicationInfo.getInstance()
                val productName = appInfo.fullApplicationName
                if (System.getProperty("os.name").contains("Windows")) {
                    if (productName.contains("IDEA")){
                        command += "idea64.exe"
                    } else if (productName.contains("PyCharm")) {
                        command += "pycharm64.exe"
                    } else if (productName.contains("Rider")) {
                        //TODO: make check for solution in demo folder
                        val root = File(taskPath)
                        val path = Path(taskPath)
                        val list = mutableListOf<File>()
                        listAllFiles(path, list)

                        for (file : File in list ) {
                            createOrUpdateSlnWithCsproj(taskPath, file.absolutePath)
                        }
                        command += "rider64.exe"
                    }
                } else if (System.getProperty("os.name").contains("Linux")) {
                    if (productName.contains("IDEA")){
                        command += "idea"
                    } else if (productName.contains("PyCharm")) {
                        command += "pycharm"
                    } else if (productName.contains("Rider")) {
                        val path = Path(taskPath)
                        val list = mutableListOf<File>()
                        listAllFiles(path, list)

                        for (file : File in list ) {
                            createOrUpdateSlnWithCsproj(taskPath, file.absolutePath)
                        }
                        command += "rider"
                    }
                //TODO: This is the Mac section. It is not possible to test functionality without a Mac
                } else {
                    if (productName.contains("IDEA")) {

                    } else if (productName.contains("PyCharm")) {

                    } else if (productName.contains("Rider")) {

                    }
                }
            }

            // how does this behave in production?
            // do we need to catch and handle or print the string returned by handleCommandLine?
            handleCommandLine(listOf(command, taskPath))
        }
    }


    @Throws(IOException::class)
    private fun listAllFiles(currentPath: Path, allFiles: MutableList<File>) {
        Files.newDirectoryStream(currentPath).use { stream ->
            for (entry in stream) {
                if (Files.isDirectory(entry)) {
                    listAllFiles(entry, allFiles)
                } else if (entry.toString().endsWith(".csproj")) {
                    allFiles.add(entry.toFile())
                }
            }
        }
    }


    /**
     * Adds a csproj file to a solution and creates a solution if it doesn't exist.
     * there is a lot of hard coded values here to get the programming 1 folder structure to work
     * with solutions. In the future this method should be changed so that it would have
     * a more broad use case
     *@param slnPath Path to the folder where the sln file is
     * @param csprojPath path to the .csproj file
     */
    private fun createOrUpdateSlnWithCsproj(slnPath: String, csprojPath: String) {

        var slnFile = File(slnPath)
        var folderGuid = UUID.randomUUID().toString().uppercase()
        val demoPathSplit = csprojPath.split(Regex("[/\\\\]"))

        var demoName = demoPathSplit[demoPathSplit.size -3]
        for (file in slnFile.listFiles()!!){
            if (file.name.endsWith(".sln")) {
                slnFile = file
                break
            }
        }

        var csprojFile = File(csprojPath)

        if (!csprojFile.exists()) {
            println("Project file not found: $csprojPath")
            return
        }
        /*
        if(!csprojFile.name.contains(demoName)) {
            csprojFile.renameTo(File(csprojFile.parent + "\\" + demoName + csprojFile.name) )
            csprojFile = File(csprojFile.parent + "\\" + demoName + csprojFile.name)
        }*/

        val projectName = csprojFile.nameWithoutExtension
        val projectGuid = UUID.randomUUID().toString().uppercase()
        val slnDir = slnFile.parentFile ?: File(".")
        var relativeCsprojPath = ""
        if (!slnFile.isFile()) {
            relativeCsprojPath = slnDir.toPath().relativize(csprojFile.toPath()).toString().replace("\\", "/")
            relativeCsprojPath = relativeCsprojPath.split("/", ignoreCase = false,limit = 2)[1]
        } else {
            relativeCsprojPath = slnDir.toPath().relativize(csprojFile.toPath()).toString().replace("\\", "/")
        }

        val projectEntry = """
        Project("{FAE04EC0-301F-11D3-BF4B-00C04F79EFBC}") = "$projectName", "$relativeCsprojPath", "{$projectGuid}"
        EndProject
    """.trimIndent()

        val folderEntry = """
        Project("{2150E333-8FDC-42A3-9474-1A3956D46DE8}") = "$demoName", "$demoName", "{$folderGuid}"
        EndProject
    """.trimIndent()


        val projectConfigSection = """
            {$projectGuid}.Debug|Any CPU.ActiveCfg = Debug|Any CPU
            {$projectGuid}.Debug|Any CPU.Build.0 = Debug|Any CPU
            {$projectGuid}.Release|Any CPU.ActiveCfg = Release|Any CPU
            {$projectGuid}.Release|Any CPU.Build.0 = Release|Any CPU
    """.trimIndent()

        if (!slnFile.isFile()) {
            slnFile = File("$slnPath/course.sln")
            println("Creating new .sln file at: ${slnFile.absolutePath}")
            val nestedProjectEntry = "    {$projectGuid} = {$folderGuid}"
            slnFile.writeText(
                buildString {
                    appendLine("Microsoft Visual Studio Solution File, Format Version 12.00")
                    appendLine(projectEntry)
                    appendLine(folderEntry)
                    appendLine("Global")
                    appendLine("GlobalSection(SolutionConfigurationPlatforms) = preSolution")
                    appendLine("Debug|Any CPU = Debug|Any CPU")
                    appendLine("Release|Any CPU = Release|Any CPU")
                    appendLine("EndGlobalSection")
                    appendLine("GlobalSection(ProjectConfigurationPlatforms) = postSolution")
                    appendLine(projectConfigSection)
                    appendLine("EndGlobalSection")
                    appendLine("GlobalSection(NestedProjects) = preSolution")
                    appendLine(nestedProjectEntry)
                    appendLine("EndGlobalSection")
                    appendLine("EndGlobal")
                }
            )
        } else {
            val lines = slnFile.readLines().toMutableList()

            // Avoid duplicate project entry
            if (lines.any { it.contains(relativeCsprojPath) }) {
                println("Project already exists in solution. Skipping.")
                return
            }

            val endProjectIndex = lines.indexOfLast { it.trim().startsWith("EndProject") }
            val globalIndex = lines.indexOfFirst { it.trim() == "Global" }


            if (endProjectIndex != -1) {
                lines.add(endProjectIndex + 1, projectEntry)
            } else {
                lines.add(projectEntry)
            }
            val projectConfigurationPlatformsIndex = lines.indexOfFirst {
                it.trim() == "GlobalSection(ProjectConfigurationPlatforms) = postSolution" }
            // Insert project configuration section at the start of project configuration
            if (globalIndex != -1 && projectConfigurationPlatformsIndex > globalIndex) {
                lines.add(projectConfigurationPlatformsIndex+1, projectConfigSection)
            }

            val nestedIndex = lines.indexOfFirst { it.trim() == "GlobalSection(NestedProjects) = preSolution" }
            if (lines.any { it.contains("\"$demoName\"") }) {
                val demoFolderIndex = lines.indexOfFirst { it.trim().contains("\"$demoName\"")  }
                val demoFolderSplit = lines[demoFolderIndex].split(",")
                var demoGuid = demoFolderSplit.last()
                demoGuid = demoGuid.replace("\"","")
                demoGuid = demoGuid.replace("{","")
                demoGuid = demoGuid.replace("}","")
                demoGuid = demoGuid.replace(" ","")
                val nestedProjectEntry = "    {$projectGuid} = {$demoGuid}"
                lines.add(nestedIndex+1,nestedProjectEntry)
            } else {
                lines.add(endProjectIndex-1,folderEntry)
                val nestedProjectEntry = "    {$projectGuid} = {$folderGuid}"
                //folder entry adds two lines to the file so we need to off set that
                lines.add(nestedIndex+2,nestedProjectEntry)
            }

            slnFile.writeText(lines.joinToString(System.lineSeparator()))
            println("Added $projectName to existing .sln at: ${slnFile.absolutePath}")
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
