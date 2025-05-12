package com.api;

import com.course.Course;
import com.course.SubTask;
import com.google.gson.*;

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
            com.api.LogHandler.logError("JsonHandler.jsonToCourses(final String jsonString)", e);
            com.api.LogHandler.logDebug(new String[]{"String jsonString"}, new String[]{jsonString});
            System.err.println(e.getMessage());
        }

        // remove invalid json data
        courseList.removeIf(course -> course.getPath() == null);
        return courseList;
    }


    /**
     * Parses Json data from a string to DemoTask objects.
     * If the Json does not map to an array of DemoTasks,
     * returns an empty list.
     *
     * @param jsonString json to parse
     * @return a list of subtasks
     */
    public List<SubTask> jsonToSubtask(final String jsonString) {
        Gson gson = new Gson();
        List<SubTask> subTaskList = new ArrayList<>();
        // Need to do a bit of digging to get to the demo tasks.
        // The top level object of the json contains a "course_parts" object,
        // which contains objects for the course demos, which contains a "tasks"
        // object, which contains the demo tasks which we want to fetch.
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
                            // Here we have a single subtask and can parse it with Gson.
                            String jsonstr = subElement.getValue().toString();
                            SubTask sub = new Gson().fromJson(jsonstr, SubTask.class);
                            subTaskList.add(sub);
                        }
                    }
                }
            } catch (NullPointerException e) {
                com.api.LogHandler.logError("JsonHandler.jsonToSubtask(final String jsonString)", e);
                com.api.LogHandler.logDebug(new String[]{"JsonObject json"},
                        new String[]{json.toString()});
                com.api.LogHandler.logInfo("JsonObject coursePart: is this null?");
            }
        } catch (JsonParseException | IllegalStateException e) {
            com.api.LogHandler.logError("JsonHandler.jsonToSubtask(final String jsonString)", e);
            com.api.LogHandler.logDebug(new String[]{"String jsonString"}, new String[]{jsonString});
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
}
