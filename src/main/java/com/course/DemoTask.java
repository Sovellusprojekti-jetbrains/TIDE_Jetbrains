package com.course;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DemoTask {

    /**
     * the Task id in timdata.
     */
    @SerializedName(value = "ide_task_id")
    private String ideTaskId;

    /**
     * the path to the course task that the demo task is a part of.
     */
    private String path;

    /**
     * filenames of the demo tasks.
     */
    private List<String> fileNames;

    /**
     * Task directory in timdata.
     */
    @SerializedName(value = "task_directory")
    private String taskDirectory;

    @SerializedName(value = "max_points")
    private float maxPoints;

    /**
     * deadline for the demo task.
     */
    @SerializedName(value = "deadline")
    private String deadLine;
    @SerializedName(value = "answer_limit")
    private String answerLimit;
    @SerializedName(value = "task_files")
    private List<TaskFile> taskFiles;


    /**
     * getter for the name of the demo task.
     * @return demo tasks name
     */
    public String getIdeTaskId() {
        return this.ideTaskId;
    }

    /**
     * getter for the path of the parent course task.
     * @return parent course task path
     */
    public String getPath() {
        return this.path;
    }


    /**
     * Get task directory.
     * @return directory as string
     */
    public String getTaskDirectory() {
        return this.taskDirectory;
    }


    /**
     * getter for the maximum amount of points you can get from a demo task.
     * @return maximum amount of points
     */
    public float getMaxPoints() {
        return this.maxPoints;
    }

    /**
     * getter for the demo task deadline.
     * @return the last date the demo task can be submitted by in ISO8601 format.
     */
    public String getDeadLine() {
        return this.deadLine;
    }
    /**
     * Makes the object into a string that only contains the ideTaskid, so that the object can be used in the treeview.
     * @return string represantation of the object
     */
    public String toString() {
        return this.ideTaskId;
    }

    /**
     * getter for the maximum amount of submits allowed for a course task.
     * @return the maximum amount of submits.
     */
    public String getAnswerLimit() {
        return this.answerLimit;
    }

    /**
     * Getter for TaskFile list.
     * @return a list of task files
     */
    public List<TaskFile> getTaskFiles() {
        return this.taskFiles;
    }

    /**
     * TaskFile read from .timdata task_file field.
     */
    public class TaskFile {
        @SerializedName(value = "task_id_ext")
        private String taskIdExt;
        @SerializedName(value = "file_name")
        private String fileName;
        @SerializedName(value = "task_directory")
        private String taskDirectory;

        /**
         * @return fileName
         */
        public String getFileName() {
            return this.fileName;
        }

        /**
         * @return taskIdExt
         */
        public String getTaskIdExt() {
            return this.taskIdExt;
        }
    }
}
