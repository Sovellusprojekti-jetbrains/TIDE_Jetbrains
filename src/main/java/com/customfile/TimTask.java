package com.customfile;

import com.course.SubTask;
import com.intellij.openapi.vfs.VirtualFile;
import com.state.ActiveState;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class "extends" VirtualFile with actions of tide task.
 */
public final class TimTask {

    private static TimTask selected; //Keep the instance of TimTask open in the editor here.
    private final VirtualFile delegate;
    private final ArrayList<String> headers;
    private final SubTask task;
    private static final HashMap<String, TimTask> CACHE = new HashMap<>();
    //TODO: Add attributes for subtask info etc.

    /**
     * In order to "extend" VirtualFile we must have one as a delegate.
     * @param file VirtualFile which is used as a delegate.
     * @param headerList Contains course name, demo name, and subtask name.
     * @param subTask Object reference to SubTask object containing all the goods.
     */
    private TimTask(VirtualFile file, ArrayList<String> headerList, SubTask subTask) {
        this.delegate = file;
        this.headers = headerList;
        this.task = subTask;
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
     * This method is used to update the information in the CourseTaskPane, and it's state etc.
     */
    private static void messageUpdates() {
        //TODO: Implement
        ActiveState.getInstance().setSubmittable(selected != null);
        ActiveState.getInstance().messageChanges();
    }

    /**
     * Getter for active instance.
     * @return TimTask.
     */
    public static TimTask getInstance() {
        return selected;
    }

    /**
     * Returns Course name.
     * @return Course name as String.
     */
    public String getCourseName() {
        return this.headers.getFirst();
    }

    /**
     * Returns Demo name.
     * @return Demo name as String.
     */
    public String getDemoName() {
        return this.headers.get(1);
    }

    /**
     * Returns SubTask name.
     * @return SubTask name as String.
     */
    public String getSubTaskName() {
        return this.headers.getLast();
    }

    /**
     * This method is called from ActiveState when MessageBus event fires.
     * @param file VirtualFile open in the editor which must be evaluated of being a tim task.
     */
    public static void evaluateFile(VirtualFile file) {
        //TODO: Make new TimTask if file open in the editor is one. Set selected null otherwise
        //TODO: Maybe TimTasks once made could be cached for later use? Edit: Why not cache all results!?
        if (file != null && file.getCanonicalPath() != null) {
            if (CACHE.containsKey(file.getUrl())) {
                selected = CACHE.get(file.getUrl());
            } else {
                ArrayList<String> taskData = new ArrayList<>();
                taskData.add(ActiveState.getInstance().getCourseName(file.getPath()));
                taskData.add(ActiveState.getInstance().findTaskName(taskData.getFirst(), file));
                SubTask taskHolder = ActiveState.getInstance().findSubTask(file);
                if (taskHolder != null) {
                    taskData.add(taskHolder.getIdeTaskId());
                    selected = new TimTask(file, taskData, taskHolder);
                    CACHE.put(file.getUrl(), selected);
                } else {
                    selected = null;
                    CACHE.put(file.getUrl(), selected);
                }
            }
        } else {
            selected = null;
        }
        //TODO: use ActiveState's setSubmittable to make actions enabled/disabled.
        messageUpdates();
    }
}
