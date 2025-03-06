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
        // Should you encounter character encoding issues
        // while running this plugin in Windows, go to:
        // region settings -> check the beta option to use
        // UTF-8.
        return this.name;
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
