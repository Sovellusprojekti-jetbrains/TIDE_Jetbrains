package com.api;

import com.course.Course;
import com.course.SubTask;
import com.google.gson.*;

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
     * @return a list of subtasks
     */
    public List<SubTask> jsonToSubtask(final String jsonString) {
        Gson gson = new Gson();
        List<SubTask> subTaskList = new ArrayList<>();
        try {
            JsonObject json = gson.fromJson(jsonString, JsonObject.class);
            JsonObject coursePart = gson.fromJson((json.getAsJsonObject("course_parts")), JsonObject.class);
            List<String> tasks = getValuesInObject(coursePart, "tasks");
            JsonObject[] objects = new JsonObject[tasks.size()];
            for (int i = 0; i < tasks.size(); i++) {
                JsonObject taskObject = gson.fromJson(tasks.get(i), JsonObject.class);
                objects[i] = taskObject;
            }
            for (JsonObject object: objects) {
                for (String currentKey : object.keySet()) {
                    Object task = object.get(currentKey);
                    JsonObject temp = gson.fromJson((object.getAsJsonObject(currentKey)), JsonObject.class); //Need to store filename into SubTask object
                    JsonArray filePart = temp.get("task_files").getAsJsonArray();
                    JsonElement temp2 = filePart.get(0); //This thing works only as long as timdata's representation doesn't change
                    JsonObject temp3 = temp2.getAsJsonObject(); //Should consider proper pattern matcher to parse Json data
                    String fileName = temp3.get("file_name").getAsString();
                    subTaskList.add(gson.fromJson(task.toString(), SubTask.class));
                    subTaskList.getLast().setFileName(fileName);
                }
            }
        } catch (JsonParseException | IllegalStateException e) {
            System.err.println(e.getMessage());
        }
        // remove invalid json data
        subTaskList.removeIf(subTask -> subTask.getIdeTaskId() == null);
        return subTaskList;
    }

    /**
     * Gets the values from a specified key from a json object.
     * @param jsonObject the object that is searched with the key
     * @param key the key that contains the searched values
     * @return list of values
     */
    public List<String> getValuesInObject(JsonObject jsonObject, String key) {
        List<String> accumulatedValues = new ArrayList<>();
        for (String currentKey : jsonObject.keySet()) {
            Object value = jsonObject.get(currentKey);
            if (currentKey.equals(key)) {
                accumulatedValues.add(value.toString());
            }
            if (value instanceof JsonObject && !currentKey.equals(key)) {
                accumulatedValues.addAll(getValuesInObject((JsonObject) value, key));
            }
        }

        return accumulatedValues;
    }

}
