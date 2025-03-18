package com.api;

import com.actions.Settings;
import com.course.SubTask;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Tänne tide cli -kutsut.
 */
public class ApiHandler {
    private final String coursesCommand = "tide courses --json";
    private final String loginCommand   = "tide login";
    private final String logoutCommand  = "tide logout";
    private final String checkLoginCommand = "tide check-login --json";
    private final String taskCreateCommand = "tide task create";
    private final String submitCommand = "tide submit";

    /**
     * Logs in to TIDE-CLI.
     */
    public void login() {
        TideCommandExecutor.INSTANCE.login();
    }


    /**
     * Logs out from TIDE-CLI.
     */
    public void logout() {
        TideCommandExecutor.INSTANCE.logout();
    }


    /**
     * Fetches IDE courses from TIM via TIDE-CLI.
     */
    public void courses() {
        TideCommandExecutor.INSTANCE.fetchCoursesAsync();
    }


    /**
     * Loads exercise into folder defined in settings.
     * @param courseDirectory Subdirectory for the course
     * @param cmdArgs Arguments for the tide create command, e.g. Tim path and flags
     */
    public void loadExercise(String courseDirectory, String... cmdArgs) throws IOException, InterruptedException {
        if (cmdArgs.length < 1) {
            System.err.println("No arguments for tide create");
        }
        File courseDirFile = new File(Settings.getPath(), courseDirectory);
        if (!courseDirFile.exists()) {
            courseDirFile.mkdir();
        }

        // Destination path is surrounded by quotes only if it contains spaces.
        // destination = destination.contains(" ") ? "\"" + destination + "\"" : destination;

        List<String> pbArgs = new ArrayList<>(Arrays.asList(taskCreateCommand.split(" ")));
        for (String arg: cmdArgs) {
            pbArgs.add(arg);
        }
        handleCommandLine(pbArgs, courseDirFile);
    }


    /**
     * This method is used to save changes in virtual file to physical file on disk.
     * @param file Virtual file
     */
    private void syncChanges(VirtualFile file) {
        FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
        Document document = fileDocumentManager.getDocument(file);
        if (document != null) {
            fileDocumentManager.saveDocument(document);
        }
    }


    /**
     * Resets subtask back to the state of latest submit.
     * @param file Virtual file to get files local path and to communicate changes to idea's UI.
     * @param courseDirectory Course directory
     * @throws IOException If .timdata file is not found or some other file reading error occurs.
     * @throws InterruptedException If TIDE CLI process fails or something else goes wrong.
     */
    public void resetSubTask(VirtualFile file, String courseDirectory) throws IOException, InterruptedException {
        String timData = com.actions.Settings.getPath() + File.separatorChar + courseDirectory
                + File.separatorChar + ".timdata"; //.timdata should be saved where the task was downloaded
        String taskData = Files.readString(Path.of(timData), StandardCharsets.UTF_8);
        JsonHandler handler = new JsonHandler();
        List<SubTask> subtasks = handler.jsonToSubtask(taskData); //List of subtasks related to a task
        String taskId = null; //base case (file open in editor is not a subtask of a task)
        String taskPath = null;
        String filePath = file.getPath();
        for (SubTask subtask : subtasks) { //finds ide_task_id and path for the subtask
            for (String name : subtask.getFileName()) {
                /* checks in case of similar filenames, not enough to handle the problem
                   if ((subtask.getIdeTaskId() == null || !filePath.contains(subtask.getIdeTaskId()))
                   && (subtask.getTaskDirectory() == null || !filePath.contains(subtask.getTaskDirectory()))) {
                      continue;
                  } */

                if (filePath.contains(name.replaceAll("\"", ""))) {
                    taskId = subtask.getIdeTaskId();
                    taskPath = subtask.getPath();
                    break;
                }

            }
            if (taskId != null) {
                break;
            }
        }

        if (taskId != null) {
            this.syncChanges(file); //sync changes before reset
            this.loadExercise(courseDirectory, taskPath, taskId, "-f");
            //Virtual file must be refreshed and intellij idea's UI notified
            file.refresh(true, true);
            ApplicationManager.getApplication().invokeLater(() -> {
                if (file.isValid()) {
                    file.getParent().refresh(false, false);
                }
            });
        } else {
            com.views.InfoView.displayError("File open in editor is not a tide task!", "task reset error");
        }
    }


    /**
     * Submit an exercise.
     * @param file Virtual file containing subtask to be submitted
     * @return Response from TIM as a string or an error message
     */
    public String submitExercise(VirtualFile file) {
        this.syncChanges(file); //sync changes before submit
        String response = "";
        try {
            List<String> commandlineArgs = new ArrayList<>(Arrays.asList(submitCommand.split(" ")));
            commandlineArgs.add(file.getPath());
            response = handleCommandLine(commandlineArgs);
        } catch (IOException | InterruptedException ex) {
            com.api.LogHandler.logError("171: ApiHandler.submitExercise(VirtualFile file)", ex);
            com.api.LogHandler.logDebug(new String[]{"171 VirtualFile file"}, new String[]{file.toString()});
            ex.printStackTrace();
            response = "IOException:" + System.lineSeparator() + ex;
        }
        return response;
    }


    /**
     * asks tide-cli if there is a login and returns a boolean. Deprecated.
     * @return login status in boolean
     */
    public boolean isLoggedIn() {
        try {
            String jsonOutput = handleCommandLine(List.of(checkLoginCommand.split(" ")));
            // Parse JSON
            Gson gson = new Gson();
            LoginOutput output = gson.fromJson(jsonOutput, LoginOutput.class);
            if (output.loggedIn != null) {
                return true;
            }

        } catch (IOException | InterruptedException ex) {
            com.api.LogHandler.logError("192: ApiHandler.isLoggedIn()", ex);
            ex.printStackTrace();
        }


        return false;
    }

    /**
     * Asks tide to check for login info asynchronously.
     */
    public void checkLogin() {
        TideCommandExecutor.INSTANCE.checkLogin();
    }


    /**
     * opens the clicked subtasks project.
     * @param taskPath path to the folder that has the clicked subtask.
     */
    public void openTaskProject(String taskPath) {
        try {
            String command = "";
            if (Objects.equals(System.getenv("DEVELOP"), "true")) {
                command = System.getenv("IDEA_LOCATION");
                var env = System.getenv();
                System.out.println(env);
            } else {
                command = PathManager.getHomePath();
            }

            handleCommandLine(List.of(command, taskPath));
        } catch (IOException | InterruptedException ex) {
            com.api.LogHandler.logError("216 ApiHandler.openTasProject(String taskPath", ex);
            com.api.LogHandler.logDebug(new String[]{"216 String taskPath"}, new String[]{taskPath});
            ex.printStackTrace();
        }
    }

    /**
     * Serialized login data.
     */
    class LoginOutput {
        @SerializedName(value = "logged_in")
        private String loggedIn;
    }


    /**
     *
     * @param command the command that is executed.
     * @return the results of the execution.
     * @throws IOException if the command is wrong or can't be executed then an error is thrown.
     */
    public String handleCommandLine(List<String> command) throws IOException, InterruptedException {
        return handleCommandLine(command, null);
    }

    /**
     * Executes a process with given arguments.
     * A working directory is needed for the TIDE command
     * 'tide task create' which is supposed to be executed
     * in the course subdirectory.
     *
     * @param command     command that is executed.
     * @param workingDirectory working directory for TIDE-CLI
     * @return the results of the execution
     * @throws IOException if the command is wrong or can't be executed then an error is thrown.
     */
    public String handleCommandLine(List<String> command, File workingDirectory) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);

        if (workingDirectory != null) {
            pb.directory(workingDirectory);
        }

        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String tideOutput = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println("Raw Output from Python:" + System.lineSeparator() + tideOutput);

        int exitCode = process.waitFor();
        System.out.println("Process exited with code: " + exitCode);

        if (exitCode != 0) {
            // Maybe there could be more advanced error reporting
            com.views.InfoView.displayError("An error occurred during TIDE call", "TIDE error");
        }

        return tideOutput;
    }
}
