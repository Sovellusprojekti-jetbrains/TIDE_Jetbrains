package com.api;

import com.course.Course;
import com.course.SubTask;
import com.google.gson.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

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

            try {
                Set<Map.Entry<String, JsonElement>> demos = coursePart.entrySet();
                for (Map.Entry entry: demos) {
                    JsonElement subtasks = (JsonElement) entry.getValue();
                    Set<Map.Entry<String, JsonElement>> subtaskMap = subtasks.getAsJsonObject().entrySet();
                    for (Map.Entry subEntry: subtaskMap) {
                        JsonObject subtasksObject = ((JsonElement) subEntry.getValue()).getAsJsonObject();
                        Set<Map.Entry<String, JsonElement>> subs = subtasksObject.entrySet();
                        for (Map.Entry subElement: subs) {
                            String jsonstr = subElement.getValue().toString();
                            SubTask sub = new Gson().fromJson(jsonstr, SubTask.class);
                            subTaskList.add(sub);
                        }
                    }
                }
            } catch (NullPointerException e) {
                com.api.LogHandler.logError("48: JsonHandler.jsonToSubtask(final String jsonString)", e);
                com.api.LogHandler.logDebug(new String[]{"52 JsonObject json"},
                        new String[]{json.toString()});
                com.api.LogHandler.logInfo("53 JsonObject coursePart: is this null?");
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

    public static String getConfigString(String name) {
        InputStream is = JsonHandler.class.getClassLoader().getResourceAsStream("config.json");
        if (is != null) {
            JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
            return jsonObject.get(name).getAsString();
        }
        return null;
    }

    public static int getConfigInt(String name) {
        InputStream is = JsonHandler.class.getClassLoader().getResourceAsStream("config.json");
        if (is != null) {
            JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
            return jsonObject.get(name).getAsInt();
        }
        return 0;
    }

    public static boolean getConfigBoolean(String name) {
        InputStream is = JsonHandler.class.getClassLoader().getResourceAsStream("config.json");
        if (is != null) {
            JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
            return jsonObject.get(name).getAsBoolean();
        }
        return false;
    }
}

