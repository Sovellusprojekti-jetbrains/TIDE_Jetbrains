package com.actions;

import com.api.ApiHandler;
import com.course.Course;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * Contains all the information the running plugin needs to synchronize.
 */
public class ActiveState {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private int count;
    private List<Course> courseList;


    /**
     * Gets the number that doesn't do anything and should be removed.
     * @return An integer.
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the pointless dummy number.
     * @param number The number to set to.
     */
    public void setCount(int number) {
        int oldCount = this.count;
        this.count = number;
        pcs.firePropertyChange("count", oldCount, number);
    }

    /**
     * Add listener for property change.
     * @param listener Listener to be added.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Remove listener for property change.
     * @param listener Listener to be removed.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Increments the count.
     */
    public void increment() {
        setCount(count + 1);
    }

    /**
     * Fetches the courses, changes the courseList property and fires an event for it.
     */
    public void updateCourses() {
        List<Course> oldCourseList = courseList;
        ApiHandler apiHandler = new ApiHandler();
        courseList = apiHandler.courses();
        pcs.firePropertyChange("courseList", oldCourseList, courseList);
    }
}

/*
    Example usage of a listener for courseList for toolwindows

    ActiveStateManager stateManager = ActiveStateManager.getInstance();
    stateManager.addStatePropertyChangeListener(new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("courseList".equals(evt.getPropertyName())) {
                // Handle the change here
                List<Course> updatedCourses = evt.getNewValue());
            }
        }
    });
 */
