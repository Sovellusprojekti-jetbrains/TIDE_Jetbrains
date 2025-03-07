package com.actions;

import com.api.ApiHandler;
import com.course.Course;
import com.intellij.openapi.application.ApplicationManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;


/**
 * Contains all the information the running plugin needs to synchronize.
 */
public class ActiveState {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private List<Course> courseList;
    private boolean isLoggedIn = false;

    /**
     * Calls the state manager for use.
     * @return The state manager.
     */
    public static ActiveState getInstance() {
        return ApplicationManager.getApplication().getService(ActiveState.class);
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
     * Fetches the courses, changes the courseList property and fires an event for it.
     */
    public void updateCourses() {
        List<Course> oldCourseList = courseList;
        ApiHandler apiHandler = new ApiHandler();
        courseList = apiHandler.courses();
        pcs.firePropertyChange("courseList", oldCourseList, courseList);
    }


    /**
     * Get the course list.
     * @return Course list
     */
    public List<Course> getCourses() {
        return this.courseList;
    }


    /**
     * Gets a course name by file path.
     * @param path File path
     * @return Course name
     */
    public String getCourseName(String path) {
        for (Course crs: courseList) {
            if (path.contains(crs.getName())) {
                return crs.getName();
            }
        }
        return "";
    }


    /**
     * Sets login state to true and fires the related event.
     */
    public void login() {
        if (!isLoggedIn) {
            isLoggedIn = true;
            pcs.firePropertyChange("login", false, isLoggedIn);
        }
    }

    /**
     * Sets login state to false and fires the related event.
     */
    public void logout() {
        if (isLoggedIn) {
            isLoggedIn = false;
            pcs.firePropertyChange("logout", true, isLoggedIn);
        }
    }

}

/*
    ActiveState stateManager = ActiveState.getInstance();
        stateManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("logout".equals(evt.getPropertyName())) {
                    hideWindow();
                }
                if ("login".equals(evt.getPropertyName())) {
                    showWindow();
                }
            }
        });
 */
