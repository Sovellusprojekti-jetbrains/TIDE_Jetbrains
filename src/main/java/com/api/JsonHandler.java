package com.api;

import com.course.Course;
import com.course.SubTask;
import com.google.gson.*;

import java.lang.reflect.Type;
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
     *
     * @param jsonString Json to parse
     * @return A list of courses
     */
    public List<Course> jsonToCourses(final String jsonString) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Course.class, new CourseDeserializer()).create();
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
     *
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
            for (String taskJson: tasks) {
                JsonObject object = gson.fromJson(taskJson, JsonObject.class);
                for (String currentKey : object.keySet()) {
                    JsonObject task = object.getAsJsonObject(currentKey);
                    SubTask sub = gson.fromJson(task, SubTask.class);
                    List<String> files = getValuesInObject(task, "file_name");
                    sub.setFileName(files);

                    List<String> taskDir = getValuesInObject(task, "task_directory");
                    if (!taskDir.isEmpty()) {
                        sub.setTaskDirectory(taskDir.get(0));
                    }

                    subTaskList.add(sub);
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
     *
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
                if (value instanceof JsonObject) {
                    accumulatedValues.addAll(getValuesInObject((JsonObject) value, key));
                } else if (value instanceof JsonArray) {
            accumulatedValues.addAll(getValuesInArray((JsonArray) value, key));
        }
        }
        return accumulatedValues;
    }

    /**
     * Gets the values from a specified key from a json array.
     * @param jsonArray the array that is searched with the key
     * @param key the key that contains the searched values
     * @return list of values.
     */
    public List<String> getValuesInArray(JsonArray jsonArray, String key) {
        List<String> accumulatedValues = new ArrayList<>();
        for (Object obj : jsonArray) {
            if (obj instanceof JsonArray) {
                accumulatedValues.addAll(getValuesInArray((JsonArray) obj, key));
            } else if (obj instanceof JsonObject) {
                accumulatedValues.addAll(getValuesInObject((JsonObject) obj, key));
            }
        }

        return accumulatedValues;
    }

    /**
     * Custom deserializer to handle invalid characters.
     */
    public class CourseDeserializer implements JsonDeserializer<Course> {
        /**
         * Custom deserializer to replace invalid characters so that course names are valid for file paths.
         * @param courseJsonElement Course element to deserialize
         * @param courseType Course type to deserialize to
         * @param context Deserialization context
         * @return A Course type object
         * @throws JsonParseException if Json is not in the expected format
         */
        @Override
        public Course deserialize(
                JsonElement courseJsonElement, Type courseType, JsonDeserializationContext context)
                throws JsonParseException {
            Course course = new Gson().fromJson(courseJsonElement.getAsJsonObject(), Course.class);

            String courseName = course.getName();
            if (courseName != null) {
                course.setName(courseName.replaceAll("[\\\\/\"?*|<>]", "-").trim());
                // rationale: "course 1: topic" -> "course 1 - topic"
                course.setName(course.getName().replaceAll("[:]\\s+", " - "));
                course.setName(course.getName().replaceAll("[:]", "-"));
            }

            return course;
        }
    }
}

