package com.customfile;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * This class "extends" VirtualFile with actions of tide task.
 */
public final class TimTask {

    private static TimTask selected; //Keep the instance of TimTask open in the editor here.
    private final VirtualFile delegate;
    //TODO: Add attributes for subtask info etc.

    /**
     * In order to "extend" VirtualFile we must have one as a delegate.
     * @param file VirtualFile which is used as a delegate.
     */
    private TimTask(VirtualFile file) {
        this.delegate = file;
    }

    /**
     * This method submits exercise to tim.
     */
    public void submit() {
        //TODO: Implement submit here.
    }

    /**
     * This method resets the exercise back to the state of latest submit.
     */
    public void resetExercise() {
        //TODO: Implement reset here.
    }

    /**
     * This method is used to show the exercise in browser.
     * @param baseURL URL for tim.
     */
    public void openInBrowser(String baseURL) {
        //TODO: Implement the action here.
    }

    /**
     * This method updates the changes in delegate VirtualFile to actual file on disk.
     */
    private void syncChanges() {
        //TODO: Implement updating the file on disk here.
    }

    /**
     * This method is used to update the information in the CourseTaskPane, and it's state.
     */
    private static void updatePane() {
        //TODO: Implement
    }

    /**
     * Getter for active instance.
     * @return TimTask.
     */
    public static TimTask getInstance() {
        return selected;
    }

    /**
     * This method is called from ActiveState when MessageBus event fires.
     * @param file VirtualFile open in the editor which must be evaluated of being a tim task.
     */
    public static void evaluateFile(VirtualFile file) {
        //TODO: Make new TimTask if file open in the editor is one. Set selected null otherwise
        //TODO: Maybe TimTasks once made could be cached for later use?
        //TODO: use ActiveState's setSubmittable to make actions enabled/disabled.
        updatePane(); //This can be used to update the CourseTaskPane. Info shown and buttons enabled/disabled.
    }
}
