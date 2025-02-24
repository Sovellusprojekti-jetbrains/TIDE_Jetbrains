package com.api;

import com.course.Course;
import com.course.SubTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    public List<Course> jsonToCourses(final String jsonString) {
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

    /**
     * Parses Json data from a string to subtask objects.
     * If the Json does not map to an array of subtasks,
     * returns an empty list.
     * @param jsonString json to parse
     * @param coursePath the path used to get the right subtasks from the jsonstring
     * @return a list of subtasks
     */
    public List<SubTask> jsonToSubtask(final String jsonString, final String coursePath) {
        //TODO: muuta käyttämään task list cmd komentoa ja käyttämään sen antamia nimiä.
        Gson gson = new Gson();
        List<SubTask> subTaskList = new ArrayList<>();
        try {
            JsonObject json = gson.fromJson(jsonString, JsonObject.class);
            JsonObject Tasks = gson.fromJson((json.getAsJsonObject("")))
            System.out.println();
        } catch (JsonParseException | IllegalStateException e) {
            System.err.println(e.getMessage());
        }
        // remove invalid json data
        subTaskList.removeIf(subTask -> subTask.getIdeTaskId() == null);
        return subTaskList;
    }
}
