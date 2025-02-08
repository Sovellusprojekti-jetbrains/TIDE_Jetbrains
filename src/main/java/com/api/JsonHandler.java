package com.api;

import com.course.Course;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates Course objects from a String of Json data.
 */
public class JsonHandler {
    /**
     * Parses Json data from a string to Course objects.
     * If the Json does not map to an array of Courses,
     * returns an empty list.
     * @param jsonString Json to parse
     * @return A list of courses
     */
    public List<Course> jsonToCourses(String jsonString) {
        Gson gson = new Gson();
        List<Course> courseList = new ArrayList<Course>();

        try {
            Course[] courseArray = gson.fromJson(jsonString, Course[].class);
            courseList = new ArrayList<Course>(Arrays.asList(courseArray));
        } catch (JsonParseException | IllegalStateException e) {
            System.err.println(e.getMessage());
        }

        // remove invalid json data
        courseList.removeIf(course -> course.getPath() == null);
        return courseList;
    }
}
