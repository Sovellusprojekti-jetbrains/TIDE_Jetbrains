package com.course;

import java.util.List;

/**
 * A class representing a course,
 * constructed by JsonHandler.
 * Contains a list of tasks.
 */
public class Course {
    /**
     * Course name.
     */
    private String name;
    /**
     * Course ID number.
     */
    private int id;
    /**
     * Course path.
     */
    private String path;
    /**
     * List of CourseTask objects.
     */
    private List<CourseTask> tasks;

    /**
     * Getter for course name.
     * @return Course name
     */
    public String getName() {
        //Quick, dirty and temporary fix to problem caused by some components
        // (this plugin, tim-server, something else...) inability to interpret char ä correctly.
        // Another characters other than ä may result to replacement
        // character also so the real problem must be found and fixed.
        return this.name.replace("�", "ä");
    }

    /**
     * Getter for course path.
     * @return Course path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Getter for course tasks.
     * @return A list of Task objects
     */
    public List<CourseTask> getTasks() {
        return this.tasks;
    }
}
