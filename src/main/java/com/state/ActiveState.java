package com.state;

import com.actions.Settings;
import com.api.ApiHandler;
import com.api.JsonHandler;
import com.api.LogHandler;
import com.api.TimDataHandler;
import com.course.Course;
import com.course.CourseDemo;
import com.course.DemoTask;
import com.customfile.TimTask;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.util.Util;
import com.views.InfoView;
import com.views.OutputWindow;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Contains all the information the running plugin needs to synchronize.
 */
public class ActiveState {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    // creating the ArrayList here should make sure it's never null
    private List<Course> courseList = new ArrayList<>();
    private String tideSubmitResponse;
    private String tideBaseResponse;
    private boolean isLoggedIn = false;
    private Project project;
    private boolean isSubmittable = false;

    /**
     * Constructor for active state with a listener to
     * print TIDE responses in InfoView bubbles.
     */
    public ActiveState() {
        this.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("tideBaseResponse".equals(evt.getPropertyName())) {
                    String response = (String) evt.getNewValue();
                    InfoView.displayInfo(response);
                }
            }
        });
    }

    /**
     * Simple method to get the login status.
     * @return Login status as boolean value.
     */
    public boolean getLogin() {
        return isLoggedIn;
    }

    /**
     * Initializes project, sets ToolWindows available, and subscribes
     * to changes in the file open in the editor. Used in TideWindowFactory
     * to make sure the plugin state is initialized properly.
     */
    public void initProjectDependents() {
        project = ProjectManager.getInstance().getOpenProjects()[0];
        ApplicationManager.getApplication().invokeLater(() -> {
            Util.setWindowAvailable(project, "Course Task", false, "/icons/timgray.svg");
            Util.setWindowAvailable(project, "Output Window", false, "/icons/timgray.svg");
        });
        project.getMessageBus().connect(Disposer.newDisposable())
                .subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
                    @Override
                    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                        if (isLoggedIn) {
                            FileEditorManagerListener.super.selectionChanged(event);
                            VirtualFile temp = event.getNewFile();
                            TimTask.evaluateFile(temp);
                        }
                    }
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
     * getter for the currently open project.
     * @return the opened project
     */
    public Project getProject() {
        return this.project;
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
        if (this.project != null) {
            var files = FileEditorManager.getInstance(this.project).getSelectedFiles();
            if (files.length > 0) {
                TimTask.evaluateFile(files[0]);
            }
        }
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
     * Reads downloaded .timdata files and creates Demo tasks accordingly.
     * @param course to get demo tasks for
     */
    public void addDownloadedDemotasksToCourse(Course course) {
        String pathToFile = Settings.getPath() + File.separatorChar + course.getName();
        JsonHandler jsonHandler = new JsonHandler();
        TimDataHandler tim = new TimDataHandler();
        String timData = tim.readTimData(pathToFile);
        if (timData.isEmpty()) {
            return;
        }
        List<DemoTask> demoTasks = jsonHandler.jsonToDemotask(timData);
        var demos = course.getTasks();
        for (CourseDemo ct: demos) {
            for (DemoTask st: demoTasks) {
                if (ct.getPath().equals(st.getPath())) {
                    ct.addDemotask(st);
                }
            }
        }
    }


    /**
     * Sets a new value for the tideSubmitResponse property.
     * Needed because response to "tide submit" gets parsed for
     * information and printed to output window, and thus needs
     * to be differentiated from the generic response represented
     * by tideBaseResponse that's printed to an InfoView bubble.
     * @param response from TIDE-CLI
     */
    public void setTideSubmitResponse(String response) {
        tideSubmitResponse = response;
        pcs.firePropertyChange("tideSubmitResponse", null, tideSubmitResponse);
        pcs.firePropertyChange("setSubmitData", null, getSubmitData());
        LogHandler.logInfo("ActiveState fired event tideSubmitResponse");
    }


    /**
     * Sets TIDE-CLI response for cases that do not need special handling.
     * Gets printed to an InfoView bubble.
     * @param response from TIDE-CLI
     */
    public void setTideBaseResponse(String response) {
        tideBaseResponse = response;
        pcs.firePropertyChange("tideBaseResponse", null, tideBaseResponse);
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

        // These are here because calling them from inside the toolwindow would not work.
        Util.setWindowAvailable(project, "Course Task", true, "/icons/tim.svg");
        Util.setWindowAvailable(project, "Output Window", true, "/icons/tim.svg");
        Util.showWindow(project, "Course Task", true);
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
        OutputWindow.getInstance().clearText();
        Util.setWindowAvailable(project, "Course Task", false, "/icons/timgray.svg");
        Util.setWindowAvailable(project, "Output Window", false, "/icons/timgray.svg");
    }

    /**
     * Getter for isSubmittable.
     * @return True, if file opened in the editor is in sub-path of task download path, False otherwise.
     */
    public boolean isSubmittable() {
        return isSubmittable;
    }

    /**
     * This method is used to find CourseDemo's name using course name and file name.
     * @param course Courses name to which the opened file is related to.
     * @param file Virtual file of the file opened in the editor.
     * @return CourseDemo name as String.
     */
    public String findTaskName(String course, VirtualFile file) {
        for (Course courseToCheck: this.getCourses()) {
            if (courseToCheck.getName().equals(course)) {
                for (CourseDemo courseDemo: courseToCheck.getTasks()) {
                    for (DemoTask demoTask: courseDemo.getDemotasks()) {
                        for (DemoTask.TaskFile taskFile: demoTask.getTaskFiles()) {
                            if (file.getPath().contains(taskFile.getFileName())) {
                                return courseDemo.getName();
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    /**
     * This method is used to find Demo Task's name (ideTaskId).
     * @param file Virtual file of the file open in the editor.
     * @return Demo task's name as String.
     */
    public DemoTask findDemoTask(VirtualFile file) {
        for (Course courseToCheck : this.getCourses()) {
            for (CourseDemo courseDemo: courseToCheck.getTasks()) {
                for (DemoTask demoTask: courseDemo.getDemotasks()) {
                    for (DemoTask.TaskFile tf: demoTask.getTaskFiles()) {
                        if (file.getPath().contains(tf.getFileName())) {
                            return demoTask;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * This method sets the state variable.
     * @param status if file in the editor can be submitted.
     */
    public void setSubmittable(Boolean status) {
        this.isSubmittable = status;
    }

    /**
     * This method is used to send messages to CourseTaskPane to change state of the buttons.
     */
    public void messageChanges() {
        if (!isSubmittable) {
            Util.setIcons(project, "/icons/timgray.svg");
            pcs.firePropertyChange("disableButtons", null, null);
        } else {
            Util.setIcons(project, "/icons/tim.svg");
            pcs.firePropertyChange("enableButtons", null, null);
            pcs.firePropertyChange("setSubmitData", null, getSubmitData());
        }
    }

    /**
     * A method that makes messages for the points, deadline and maximum number of submits.
     * @return string array of messages
     */
    public String[] getSubmitData() { //Moved to TimTask
        return TimTask.getInstance().getSubmitData();
    }


    /**
     * Used to notify CourseMainPane of scroll speed change in settings.
     */
    public void signalScrollSpeedUpdate() {
        pcs.firePropertyChange("scrollSpeed", null, null);
    }


    /**
     * Used to notify SettingsScreen of change in browser setting to facilitate JRadioButton update on settings revert.
     */
    public void signalBrowserChoiceUpdate() {
        pcs.firePropertyChange("browserSetting", null, null);
    }
}
