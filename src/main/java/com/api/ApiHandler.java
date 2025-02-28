package com.api;

import com.actions.Settings;
import com.course.Course;
import com.course.SubTask;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
    private final String taskOpenCommand = "idea64.exe";

    /**
     * Logs in to TIDE-CLI.
     * @throws IOException Method calls pb.start() and pb.readLine() may throw IOException
     * @throws InterruptedException Method call process.waitFor() may throw InterruptedException
     */
    public void login() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(loginCommand.split("\\s+"));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();
        System.out.println("Process exited with code: " + exitCode);
    }


    /**
     * Logs out from TIDE-CLI.
     * @throws IOException Method calls pb.start() and pb.readLine() may throw IOException
     * @throws InterruptedException Method call process.waitFor() may throw InterruptedException
     */
    public void logout() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(logoutCommand.split("\\s+"));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();
        System.out.println("Process exited with code: " + exitCode);
    }


    /**
     * Fetches IDE courses from TIM via TIDE-CLI.
     * @return A list of Course objects
     */
    public List<Course> courses() {
        StringBuilder jsonString = new StringBuilder();

        try {
            ProcessBuilder pb = new ProcessBuilder(coursesCommand.split("\\s+"));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

        JsonHandler handler = new JsonHandler();
        List<Course> courses = handler.jsonToCourses(jsonString.toString());

        return courses;
    }

    /**
     * Loads exercise into folder defined in settings.
     * @param timPath Path of the exercise in TIM
     */
    public void loadExercise(String timPath) throws IOException, InterruptedException {
        String destination = Settings.getPath();
        // Destination path is surrounded by quotes only if it contains spaces.
        String command = this.taskCreateCommand + " " + timPath + " -d "
                + (destination.contains(" ") ? "\"" + destination + "\"" : destination);
        ProcessBuilder pb = new ProcessBuilder(command.split("\\s+"));
        // Without the following, is it assumed that destination folder is in sub path of plugin's working directory or something like that.
        // The process will exit with exit code 1 when it discovers that files are saved elsewhere
        pb.directory(new File(destination));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); //Debug stuff
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();
        System.out.println("Process exited with code: " + exitCode);
        if (exitCode != 0) {
            // Maybe there could be more advanced error reporting
            com.views.InfoView.displayError("An error occurred during download", "Download error");
        }
    }

    /**
     * Overload method.
     * @param timPath Path of the exercise in TIM
     * @param flag Determines which task will be downloaded and how (overwrite or not)
     */
    public void loadExercise(String timPath, String flag) throws IOException, InterruptedException {
        this.loadExercise(" " + timPath + " " + flag);
    }

    /**
     * Resets subtask back to the state of latest submit.
     * @param path local path of the subtask.
     * @param file Virtual file to get files local path and to communicate changes to idea's UI.
     * @throws IOException If .timdata file is not found or some other file reading error occurs.
     * @throws InterruptedException If TIDE CLI process fails or something else goes wrong.
     */
    public void resetSubTask(String path, VirtualFile file) throws IOException, InterruptedException {
        String timData = com.actions.Settings.getPath() + "/.timdata"; //.timdata should be saved where the task was downloaded
        String taskData = Files.readString(Path.of(timData), StandardCharsets.UTF_8);
        JsonHandler handler = new JsonHandler();
        List<SubTask> subtasks = handler.jsonToSubtask(taskData); //List of subtasks related to a task
        String taskId = null; //base case (file open in editor is not a subtask of a task)
        String taskPath = null;
        for (SubTask subtask : subtasks) { //finds ide_task_id and path for the subtask
            for (String name : subtask.getFileName()) {
                if (path.contains(name.replaceAll("\"", ""))) {
                    taskId = subtask.getIdeTaskId();
                    taskPath = subtask.getPath();
                    break;
                }
            }
        }
        if (taskId != null) {
            this.loadExercise(taskPath + " " + taskId, "-f");
            if (file != null) { //Virtual file must be refreshed and intellij idea's UI notified
                file.refresh(true, true);
                ApplicationManager.getApplication().invokeLater(() -> {
                    if (file.isValid()) {
                        file.getParent().refresh(false, false);
                    }
                });
            }
        } else {
            com.views.InfoView.displayError("File open in editor is not a tide task!", "task reset error");
        }
    }

    /**
     * Submit an exercise.
     * @param exercisePath Path of the file to be submitted
     * @return Response from TIM as a string or an error message
     */
    public String submitExercise(String exercisePath) {
        String response = "";
        try {
            String command = submitCommand + " " + exercisePath;
            ProcessBuilder pb = new ProcessBuilder(command.split("\\s+"));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            response = reader.lines().collect(Collectors.joining("\n"));
            System.out.println("Raw Output from Python:\n" + response);
        } catch (IOException ex) {
            ex.printStackTrace();
            response = "IOException:\r\n" + ex;
        }
        return response;
    }

    /**
     * asks tide-cli if there is a login and returns a boolean.
     * @return login status in boolean
     */
    public boolean  isLoggedIn() {
        try {

            ProcessBuilder pb = new ProcessBuilder(checkLoginCommand.split("\\s+"));
            pb.redirectErrorStream(true);

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String jsonOutput = reader.lines().collect(Collectors.joining("\n"));
            System.out.println("Raw Output from Python: " + jsonOutput);
            // Parse JSON
            Gson gson = new Gson();
            LoginOutput output = gson.fromJson(jsonOutput, LoginOutput.class);
            if (output.loggedIn != null) {
                return true;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }


        return false;
    }

    /**
     * opens the clicked subtasks project.
     * @param taskPath path to the folder that has the clicked subtask.
     */
    public void openTaskProject(String taskPath) {
        try {
            String command = taskOpenCommand + " " + taskPath;
            ProcessBuilder pb = new ProcessBuilder(command.split("\\s+"));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);
    } catch (IOException | InterruptedException ex) {
        ex.printStackTrace();
    }
    }

    class LoginOutput {
        @SerializedName(value = "logged_in")
        private String loggedIn;
    }

}
