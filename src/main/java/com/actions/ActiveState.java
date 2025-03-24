package com.actions;

import com.api.ApiHandler;
import com.api.LogHandler;
import com.course.Course;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.views.OutputWindow;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;


/**
 * Contains all the information the running plugin needs to synchronize.
 */
public class ActiveState {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private List<Course> courseList;
    private String tideSubmitResponse;
    private String tideBaseResponse;
    private boolean isLoggedIn = false;
    private Project project;

    /**
     * Constructor for active state attempts to hide the right and bottom toolwindows.
     */
    public ActiveState() {
        project = ProjectManager.getInstance().getOpenProjects()[0];
        ApplicationManager.getApplication().invokeLater(() -> {
            hideWindow("Course Task");
            hideWindow("Output Window");
        });
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
        ApplicationManager.getApplication().invokeLater(() -> {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow window = toolWindowManager.getToolWindow(id);
            window.setIcon(IconLoader.getIcon("/icons/timgray.svg"));
            //assert window != null;
            window.setAvailable(false);
        });
    }

    /**
     * Makes the toolwindow available.
     * @param id String of the toolwindow.
     */
    private void showWindow(String id) {
        ApplicationManager.getApplication().invokeLater(() -> {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow window = toolWindowManager.getToolWindow(id);
            window.setIcon(IconLoader.getIcon("/icons/tim.svg"));
            //assert window != null;
            window.setAvailable(true);
        });
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
     * Fetches the courses asynchronously.
     */
    public void updateCourses() {
        ApiHandler apiHandler = new ApiHandler();
        apiHandler.courses();
    }

    /**
     * Use this to change the list of courses inside ActiveState. Fires a "courseList" event.
     * @param courses List of courses to change to.
     */
    public void setCourses(List<Course> courses) {
        List<Course> oldCourseList = courseList;
        courseList = courses;
        pcs.firePropertyChange("courseList", oldCourseList, courseList);
        LogHandler.logInfo("ActiveState fired event courseList");
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
     * Sets a new value for the tideSubmitResponse property.
     * Needed because response to tide submit gets parsed for
     * information and thus needs to be differentiated from
     * the generic response represented by tideBaseResponse.
     * @param response from TIDE-CLI
     */
    public void setTideSubmitResponse(String response) {
        String oldTideSubmitResponse = tideSubmitResponse;
        tideSubmitResponse = response;
        pcs.firePropertyChange("tideSubmitResponse", oldTideSubmitResponse, tideSubmitResponse);
        LogHandler.logInfo("ActiveState fired event tideSubmitResponse");
        setTideBaseResponse(response);
    }


    /**
     * Sets TIDE-CLI response for cases that do not need special handling.
     * @param response from TIDE-CLI
     */
    public void setTideBaseResponse(String response) {
        OutputWindow.getInstance().showWindow();
        String oldTideBaseResponse = tideBaseResponse;
        tideBaseResponse = response;
        pcs.firePropertyChange("tideBaseResponse", oldTideBaseResponse, tideBaseResponse);
        LogHandler.logInfo("ActiveState fired event tideBaseResponse");
    }


    /**
     * Sets login state to true and fires the related event.
     */
    public void login() {
        if (!isLoggedIn) {
            isLoggedIn = true;
        }
        pcs.firePropertyChange("login", false, isLoggedIn);
        LogHandler.logInfo("ActiveState fired event login");
        showWindow("Course Task");
        showWindow("Output Window");
    }

    /**
     * Sets login state to false and fires the related event.
     */
    public void logout() {
        if (isLoggedIn) {
            isLoggedIn = false;
        }
        pcs.firePropertyChange("logout", true, isLoggedIn);
        LogHandler.logInfo("ActiveState fired event logout");
        hideWindow("Course Task");
        hideWindow("Output Window");
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
