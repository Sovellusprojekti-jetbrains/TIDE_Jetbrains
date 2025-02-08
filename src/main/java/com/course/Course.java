package com.course;

import java.util.List;

/**
 * A class representing a course,
 * constructed by JsonHandler.
 * Contains a list of tasks.
 */
public class Course {
    private String name;
    private int id;
    private String path;
    private List<CourseTask> tasks;

    public String getName() { return this.name; }
    public String getPath() { return this.path; }
    public List<CourseTask> getTasks() { return this.tasks; }
}