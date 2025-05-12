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
     * List of CourseDemo objects.
     */
    private List<CourseDemo> tasks;

    /**
     * Getter for course name.
     * Should you encounter character encoding issues
     * while running this plugin in Windows, go to:
     * region settings -> check the beta option to use
     * UTF-8. Note: this advice is also found in the
     * user documentation.
     * @return Course name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for name attribute.
     * @param newName New name
     */
    public void setName(String newName) {
        this.name = newName;
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
    public List<CourseDemo> getTasks() {
        return this.tasks;
    }
}
