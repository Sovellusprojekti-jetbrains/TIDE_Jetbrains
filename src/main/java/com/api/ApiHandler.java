package com.api;

import com.course.Course;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Tänne tide cli -kutsut.
 */
public class ApiHandler {
    private final String COURSES_COMMAND = "tide courses --json";
    private final String LOGIN_COMMAND   = "tide login";
    private final String LOGOUT_COMMAND  = "tide logout";

    /**
     * Logs in to TIDE-CLI.
     * @throws IOException Method calls pb.start() and pb.readLine() may throw IOException
     * @throws InterruptedException Method call process.waitFor() may throw InterruptedException
     */
    public void login() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(LOGIN_COMMAND.split("\\s+"));
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
        ProcessBuilder pb = new ProcessBuilder(LOGOUT_COMMAND.split("\\s+"));
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
            ProcessBuilder pb = new ProcessBuilder(COURSES_COMMAND.split("\\s+"));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.ISO_8859_1));
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
}
