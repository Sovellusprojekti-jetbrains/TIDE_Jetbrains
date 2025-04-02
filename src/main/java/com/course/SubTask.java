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

    /**
     * set the task name for the task.
     * @param id the name of the task
     */
    public void setIdeTaskId(String id) {
        this.ideTaskId = id;
    }

    /**
     * sets the path of the course that the task belongs to.
     * @param coursePath path to the Course
     */
    public void setPath(String coursePath) {
        this.path = coursePath;
    }

    /**
     * getter for the name of the subtask.
     * @return subtask name
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
     * setter for the file names of a subtask.
     * @param name file name as String
     */
    public void setFileName(List<String> name) {
        this.fileNames = name;
    }

    /**
     * getter for the file names of a subtask.
     * @return file name as String
     */
    public List<String> getFileName() {
        return this.fileNames;
    }

    /**
     * Get task directory.
     * @return directory as string
     */
    public String getTaskDirectory() {
        return this.taskDirectory;
    }

    /**
     * Sets task directory.
     * @param taskDir new task directory
     */
    public void setTaskDirectory(String taskDir) {
        this.taskDirectory = taskDir;
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
}
