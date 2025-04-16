package com.customfile;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * This class "extends" VirtualFile with actions of tide task.
 */
public class TimTask {

    private final VirtualFile delegate;

    /**
     * In order to "extend" VirtualFile we must have one as a delegate.
     * @param file VirtualFile which is used as a delegate.
     */
    public TimTask(VirtualFile file) {
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
    public void syncChanges() {
        //TODO: Implement updating the file on disk here.
    }
}
