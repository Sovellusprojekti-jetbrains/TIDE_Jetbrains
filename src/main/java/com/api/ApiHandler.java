package com.api;

import com.course.DemoTask;
import com.intellij.openapi.vfs.VirtualFile;
import com.interfaces.UserManagement;

import java.io.*;

/**
 * here are the tide commands.
 */
public class ApiHandler implements UserManagement {
    private TideCommandExecutor tideCommandExecutor;

    /**
     * Constructor sets a singleton of TideCommandExecutor to a variable. This is for mocking purposes.
     */
    public ApiHandler() {
        tideCommandExecutor = TideCommandExecutor.INSTANCE;
    }

    /**
     * Sets the tide command executor to a new TideCommandExecutor, for mocking purposes.
     * @param executor the tideCommandExecutor for handling tide commands
     */
    public void setTideCommandExecutor(TideCommandExecutor executor) {
        tideCommandExecutor = executor;
    }

    /**
     * Logs in to TIDE-CLI.
     */
    public void login() {
        tideCommandExecutor.login();
    }


    /**
     * Logs out from TIDE-CLI.
     */
    public void logout() {
        tideCommandExecutor.logout();
    }


    /**
     * Fetches IDE courses from TIM via TIDE-CLI.
     */
    public void courses() {
        tideCommandExecutor.fetchCoursesAsync();
    }


    /**
     * Loads exercise into folder defined in settings.
     * @param courseDirectory Subdirectory for the course
     * @param cmdArgs Arguments for the tide create command, e.g. Tim path and flags
     */
    public void loadExercise(String courseDirectory, String... cmdArgs) throws IOException, InterruptedException {
        tideCommandExecutor.loadExercise(courseDirectory, cmdArgs);
    }


    /**
     * Resets subtask back to the state of latest submit.
     * @param task to reset.
     * @param courseDirectory Course directory
     * @throws IOException If .timdata file is not found or some other file reading error occurs.
     * @throws InterruptedException If TIDE CLI process fails or something else goes wrong.
     */
    public void resetSubTask(DemoTask task, String courseDirectory) throws IOException, InterruptedException {
        tideCommandExecutor.resetSubTask(task, courseDirectory);
    }


    /**
     * Submit an exercise.
     * @param file Virtual file containing subtask to be submitted
     */
    public void submitExercise(VirtualFile file) {
        tideCommandExecutor.submitExercise(file);
    }


    /**
     * opens the clicked subtasks project.
     * @param taskPath path to the folder that has the clicked subtask.
     */
    public void openTaskProject(String taskPath) {
        tideCommandExecutor.openTaskProject(taskPath);
    }


}
