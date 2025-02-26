package com.course;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A class representing a task,
 * constructed by JsonHandler.
 */
public class CourseTask {
    /**
     * Course name.
     */
    private String name;
    /**
     * Course ID number.
     */
    @SerializedName(value = "doc_id")
    private int id;
    /**
     * Course path.
     */
    private String path;

    /**
     *  List of exercises in the task.
     */
    private List<SubTask> tasks;

    /**
     * Getter for task name.
     * @return Task name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for task path.
     * @return Task path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Getter for the exercises in the task.
     * @return List of exercises
     */
    public List<SubTask> getExercises() {
        return this.tasks;
    }

    /**
     * setter for the subtasks of the Course task.
     * @param subTasks list of subtasks for the Course task
     */
    public void setTasks(List<SubTask> subTasks) {
        this.tasks = subTasks;
    }
}
