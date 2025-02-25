package com.course;

import com.google.gson.annotations.SerializedName;

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
}
