package com.course;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubTask {

    /**
     * the Task id in timdata.
     */
    @SerializedName(value = "ide_task_id")
    private String ideTaskId;

    /**
     * the path to the course task that the subtask is a part of.
     */
    private String path;

    /**
     * filenames of the subtasks.
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
     * deadline for the subtask.
     */
    @SerializedName(value = "deadline")
    private String deadLine;
    @SerializedName(value = "answer_limit")
    private int answerLimit;
    @SerializedName(value = "task_files")
    private List<TaskFile> taskFiles;
    @SerializedName(value = "stem")
    private String stem;


    /**
     * getter for the name of the subtask.
     * @return subtask name
     */
    public String getIdeTaskId() {
        return this.ideTaskId;
    }

    /**
     *
     * @return
     */
    public String getStem() {
        return this.stem;
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
     * getter for the maximum amount of points you can get from a subtask.
     * @return maximum amount of points
     */
    public float getMaxPoints() {
        return this.maxPoints;
    }

    /**
     * getter for the subtask deadline.
     * @return the last date the subtask can be submitted by in ISO8601 format.
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
    public int getAnswerLimit() {
        return this.answerLimit;
    }

    /**
     * Getter for TaskFile list.
     * @return a list of task files
     * TODO: rethink naming of task, subtask, taskfile
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
