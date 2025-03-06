package com.actions;

import com.api.ApiHandler;
import com.course.Course;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;

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
    Project project;

    public ActiveState() {
        project = ProjectManager.getInstance().getOpenProjects()[0];
        hideWindow("Course Task");
        hideWindow("Output Window");
    }


    /**
     * Calls the state manager for use.
     * @return The state manager.
     */
    public static ActiveState getInstance() {
        return ApplicationManager.getApplication().getService(ActiveState.class);
    }

    /**
     * Makes the toolwindow unavailable.
     * @param id String of the toolwindow.
     */
    private void hideWindow(String id) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow window = toolWindowManager.getToolWindow(id);
        assert window != null;
        window.setAvailable(false);
    }

    /**
     * Makes the toolwindow available.
     * @param id String of the toolwindow.
     */
    private void showWindow(String id) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow window = toolWindowManager.getToolWindow(id);
        assert window != null;
        window.setAvailable(true);
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
     * Sets login state to true and fires the related event.
     */
    public void login() {
        if (!isLoggedIn) {
            isLoggedIn = true;
            pcs.firePropertyChange("login", false, isLoggedIn);
            showWindow("Course Task");
            showWindow("Output Window");
        }
    }

    /**
     * Sets login state to false and fires the related event.
     */
    public void logout() {
        if (isLoggedIn) {
            isLoggedIn = false;
            pcs.firePropertyChange("logout", true, isLoggedIn);
            hideWindow("Course Task");
            hideWindow("Output Window");
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
