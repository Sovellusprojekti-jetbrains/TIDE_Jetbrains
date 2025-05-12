package com.course;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a demo,
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
     *  List of exercises in the demo.
     */
    private List<DemoTask> tasks;

    /**
     * Getter for demo name.
     * @return Demo name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for Demo path.
     * @return Demo path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Getter for the exercises in the demo.
     * @return List of exercises
     */
    public List<DemoTask> getDemotasks() {
        if (this.tasks == null) {
            this.tasks = new ArrayList<DemoTask>();
        }
        return this.tasks;
    }

    /**
     * setter for the Demo tasks of the Course Demo.
     * @param demoTasks list of demotasks for the  CourseDemo
     */
    public void setTasks(List<DemoTask> demoTasks) {
        this.tasks = demoTasks;
    }

    /**
     * Add a demo task for a course demo.
     * @param demoTask The demo task to add
     */
    public void addDemotask(DemoTask demoTask) {
        if (this.tasks == null) {
            this.tasks = new ArrayList<DemoTask>();
        }
        this.tasks.add(demoTask);
    }

    /**
     * setter the course that the demo belongs to.
     * @param course the course that the task is part of
     */
    public void setParent(Course course) {
        this.parent = course;
    }

    /**
     * Returns the course that the demo belongs to.
     * @return the course that the demo belongs to
     */
    public Course getParent() {
        return this.parent;
    }
}
