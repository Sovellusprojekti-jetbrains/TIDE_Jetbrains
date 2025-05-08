package com.course;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a task,
 * constructed by JsonHandler.
 */
public class CourseDemo {
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

    private Course parent;
    /**
     *  List of exercises in the task.
     */
    private List<DemoTask> tasks;

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
    public List<DemoTask> getSubtasks() {
        if (this.tasks == null) {
            this.tasks = new ArrayList<DemoTask>();
        }
        return this.tasks;
    }

    /**
     * setter for the subtasks of the Course task.
     * @param subTasks list of subtasks for the Course task
     */
    public void setTasks(List<DemoTask> subTasks) {
        this.tasks = subTasks;
    }

    /**
     * Add a subtask for a coursetask.
     * @param subTask The subtask to add
     */
    public void addSubtask(DemoTask subTask) {
        if (this.tasks == null) {
            this.tasks = new ArrayList<DemoTask>();
        }
        this.tasks.add(subTask);
    }

    /**
     * setter the course that the task belongs to.
     * @param course the course that the task is part of
     */
    public void setParent(Course course) {
        this.parent = course;
    }

    /**
     * Returns the course that the task belongs to.
     * @return the course that the task belongs to
     */
    public Course getParent() {
        return this.parent;
    }
}
