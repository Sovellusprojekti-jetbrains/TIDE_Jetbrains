package com.course;


import com.google.gson.annotations.SerializedName;

public class SubTask {

    /**
     * the Task id in timdata.
     */
    @SerializedName(value = "ide_task_id")
    private String ideTaskId;

    /**
     * the path to the course task that.
     */
    private String path;

    /**
     * Filename of an individual subtask.
     */
    private String fileName;



    /**
     * set the task name for the task.
     * @param id the name of the task
     */
    public void setIdeTaskId(String id) {
        this.ideTaskId = id;
    }

    /**
     * constructor for a subtask.
     * @param id task id
     * @param coursePath path to the course that the task belongs to
     */
    public SubTask(String id, String coursePath) {
        this.ideTaskId = id;
        this.path = coursePath;
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
     * setter for the file name of a subtask.
     * @param name file name as String
     */
    public void setFileName(String name) {
        this.fileName = name;
    }

    /**
     * getter for the file name of a subtask.
     * @return file name as String
     */
    public String getFileName() {
        return this.fileName;
    }
}
