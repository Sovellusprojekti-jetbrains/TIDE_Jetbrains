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
            com.api.LogHandler.logError("23 JsonHandler.jsonToCourses(final String jsonString)", e);
            com.api.LogHandler.logDebug(new String[]{"23 String jsonString"}, new String[]{jsonString});
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
            List<String> tasks = new ArrayList<>();

            try {
                tasks = getValuesInObject(coursePart, "tasks");
            } catch (NullPointerException e) {
                com.api.LogHandler.logError("48: JsonHandler.jsonToSubtask(final String jsonString)", e);
                com.api.LogHandler.logDebug(new String[]{"52 JsonObject json"},
                        new String[]{json.toString()});
                com.api.LogHandler.logInfo("53 JsonObject coursePart: is this null?");
            }

            for (String taskJson: tasks) {
                JsonObject object = gson.fromJson(taskJson, JsonObject.class);
                for (String currentKey : object.keySet()) {
                    JsonObject task = object.getAsJsonObject(currentKey);
                    SubTask sub = gson.fromJson(task, SubTask.class);
                    List<String> files = getValuesInObject(task, "file_name");
                    sub.setFileName(files);

                    // Get task_directory field from Json.
                    // If task_directory is defined within task_files,
                    // it overrides the one defined in the task object.
                    // This implementation relies on the assumption that task_files
                    // always comes before task_directory, as the first non-null element
                    // in taskDirs is chosen.
                    List<String> taskDirs = getValuesInObject(task, "task_directory");
                    if (!taskDirs.isEmpty()) {
                        for (String dir: taskDirs) {
                            if (!dir.equals("null")) {
                                sub.setTaskDirectory(removeQuotes(dir));
                                break;
                            }
                        }
                    }

                    subTaskList.add(sub);
                }
            }
        } catch (JsonParseException | IllegalStateException e) {
            com.api.LogHandler.logError("49 JsonHandler.jsonToSubtask(final String jsonString)", e);
            com.api.LogHandler.logDebug(new String[]{"49 String jsonString"}, new String[]{jsonString});
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
                accumulatedValues.add(removeQuotes(value.toString()));
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
     * Custom deserializer to ensure course names are valid for file paths.
     */
    public class CourseDeserializer implements JsonDeserializer<Course> {
        /**
         * Custom deserializer to replace invalid characters so that course names are valid for file paths.
         *
         * @param courseJsonElement Course element to deserialize
         * @param courseType        Course type to deserialize to
         * @param context           Deserialization context
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
                                       // replace colon preceded by non-whitespace and followed by whitespace
                                       // rationale: "course 1: topic" -> "course 1 - topic"
                courseName = courseName.replaceAll("[:](?<=\\S)\\s+", " - ")
                                       // replace \, /, ", ?, *, |, <, > and remaining colons
                                       .replaceAll("[\\\\/\"?*|<>:]", "-")
                                       .trim();
                course.setName(courseName);
            }

            return course;
        }
    }


    /**
     * Remove quotes from a string if it starts and ends with one.
     * @param str String to process
     * @return Resulting string
     */
    private String removeQuotes(String str) {
        String result = str;
        if (result.startsWith("\"") && result.endsWith("\"")) {
            result = result.substring(1, result.length() - 1);
        }
        return result;
    }
}

