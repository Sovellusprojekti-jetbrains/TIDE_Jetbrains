package com.api;

import com.actions.Settings;
import com.course.Course;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import com.factories.OutputWindowFactory;

/**
 * Tänne tide cli -kutsut.
 */
public class ApiHandler {
    private final String coursesCommand = "tide courses --json";
    private final String loginCommand   = "tide login";
    private final String logoutCommand  = "tide logout";
    private final String checkLoginCommand = "tide check-login --json";
    private final String taskCreateCommand = "tide task create --all";
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
                OutputWindowFactory.getInstance().printText(line); //TODO: poista
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
        // Probably safest to surround destination path with quotes in case it contains white space characters
        String command = this.taskCreateCommand + " " + timPath +  " -d \"" + destination + "\"";
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
            com.views.ErrorView.displayError("An error occurred during download", "Download error");
        }
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

    class LoginOutput {
        @SerializedName(value = "logged_in")
        private String loggedIn;
    }

}
