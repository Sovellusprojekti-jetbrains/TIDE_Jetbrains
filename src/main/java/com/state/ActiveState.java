package com.state;

import com.actions.Settings;
import com.api.ApiHandler;
import com.api.LogHandler;
import com.course.Course;
import com.course.CourseTask;
import com.course.SubTask;
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
import org.jdesktop.swingx.action.ActionManager;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;


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
    private boolean isSubmittable = false;
    private List<SubTask> subTaskList;

    /**
     * Constructor for active state attempts to hide the right and bottom toolwindows.
     */
    public ActiveState() {
        project = ProjectManager.getInstance().getOpenProjects()[0];
        ApplicationManager.getApplication().invokeLater(() -> {
            Util.setWindowAvailable(project, "Course Task", false, "/icons/timgray.svg");
            Util.setWindowAvailable(project, "Output Window", false, "/icons/timgray.svg");
        });
        project.getMessageBus().connect(Disposer.newDisposable())
                .subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
            @Override
            public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                FileEditorManagerListener.super.selectionChanged(event);
                try {
                    VirtualFile temp = event.getNewFile();
                    if (temp != null) {
                        setSubmittable(temp);
                    } else { //Is it possible to construct new Virtual file with null canonical path?
                        //It would be better if it was possible to call setSubmittable with null as the argument
                        isSubmittable = false;
                        messageChanges();
                        messageTaskName(" ", " ", " ");
                    }
                } catch (IOException e) { //Should never happen.
                    throw new RuntimeException(e);
                }
            }
        });
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
        pcs.firePropertyChange("tideSubmitResponse", null, tideSubmitResponse);
        pcs.firePropertyChange("setSubmitData", null, getSubmitData());
        LogHandler.logInfo("ActiveState fired event tideSubmitResponse");
        setTideBaseResponse(response);
    }


    /**
     * Sets TIDE-CLI response for cases that do not need special handling.
     * @param response from TIDE-CLI
     */
    public void setTideBaseResponse(String response) {
        String oldTideBaseResponse = tideBaseResponse;
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
     * This method checks if the opened file is .timdata or some log file.
     * @param file Vrtual file in inspection.
     * @return true if allowed file, false otherwise.
     */
    private boolean allowedName(VirtualFile file) {
        String[] blacklist = {".timdata", "log"};
        for (String name: blacklist) {
            if (file.getName().contains(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is used to find CourseTask's name using course name and file name.
     * @param course Courses name to which the opened file is related to.
     * @param file Virtual file of the file opened in the editor.
     * @return CourseTask name as String.
     */
    private String findTaskName(String course, VirtualFile file) {
        Course courseTemp = null;
        for (Course temp: this.courseList) {
            if (temp.getName().equals(course)) {
                courseTemp = temp;
                break;
            }
        }
        if (courseTemp != null) {
            SubTask subTaskTemp = null;
                for (SubTask temp2 : this.subTaskList) {
                    if (file.getPath().contains(temp2.getFileName().get(0))) {
                        subTaskTemp = temp2;
                        break;
                    }
                }
            if (subTaskTemp != null) {
                for (CourseTask temp3 : courseTemp.getTasks()) {
                    if (temp3.getPath().equals(subTaskTemp.getPath())) {
                        return temp3.getName();
                    }
                }
            }
        }
        return "";
    }

    /**
     * This method is used to find SubTask's name (ideTaskId).
     * @param file Virtual file of the file open in the editor.
     * @return Subtask's name as String.
     */
    private String findSubTaskName(VirtualFile file) {
        for (SubTask task : this.subTaskList) {
            if (file.getPath().contains(task.getFileName().get(0))) {
                return task.getIdeTaskId();
            }
        }
        return "";
    }

    /**
     * This method evaluates if the file opened in the editor is in sub-path of task download path.
     * @param child File under evaluation should be child of task download folder.
     * @throws IOException If making File object fails.
     */
    public void setSubmittable(VirtualFile child) throws IOException {
        File parent = new File(Settings.getPath());
        if (child.getCanonicalPath() != null && this.allowedName(child)) {
            this.isSubmittable = child.getCanonicalPath()
                    .replaceAll("/", Matcher.quoteReplacement(File.separator))
                    .contains(parent.getCanonicalPath());
        } else {
            this.isSubmittable = false;
        }
        if (this.isSubmittable) { // Updates the info displayed on CourseTaskPane.
            String course = this.getCourseName(child.getPath());
            String demo = this.findTaskName(course, child);
            String sub = this.findSubTaskName(child);
            this.messageTaskName(course, demo, sub);
        }
        this.messageChanges();
    }

    /**
     * This method is used to send messages to CourseTaskPane to change state of the buttons.
     */
    public void messageChanges() {
        ActionManager.getInstance().getAction("Reset Exercise"); //Actions must be able/disabled also
        if (!isSubmittable) {
            //Is null ok or should one send isSubmittable values?
            pcs.firePropertyChange("disableButtons", null, null);
        } else {
            pcs.firePropertyChange("enableButtons", null, null);
            pcs.firePropertyChange("setSubmitData", null, getSubmitData());
        }
    }

    /**
     * Messages new values to the CourseTaskPane.
     * @param course Name of the course.
     * @param task CourseTask.
     * @param subtask subtask.
     */
    private void messageTaskName(String course, String task, String subtask) {
        String[] values = {course, task, subtask};
        pcs.firePropertyChange("setDemoName", null, values);
    }

    /**
     * Sets the subTask list. The implementation is weird because CourseMainPane might call this with smaller list than
     * was set a moment ago. During tree view update JsonData is read and objects created multiple times.
     * @param subTasks List of subtasks.
     */
    public void setSubTasks(List<SubTask> subTasks) {
        if (this.subTaskList == null) {
            this.subTaskList = subTasks;
        } else {
            //TODO: fix the bloating of the subtasklist. JSON should only be read again if new tasks are donwloaded.
            this.subTaskList.addAll(subTasks);
        }
    }

    /**
     * This method returns the subtask object instance of the open task file.
     * from the file path on the local disk drive.
     * @param filePath File's path on disk.
     * @return SubTask object
     */
    public SubTask getOpenTask(String filePath) {
        for (SubTask task : this.subTaskList) {
            if (filePath.contains(task.getFileName().get(0))) {
                return task;
            }
        }
        return null;
    }

    /**
     * a method that makes messages for the points, deadline and maximum number of submits.
     * @return string array of messages
     */
    public String[] getSubmitData() {
        StateManager state = new StateManager();
        VirtualFile file = FileEditorManager
                .getInstance(project)
                .getSelectedEditor()
                .getFile();
        SubTask current = getOpenTask(file.getCanonicalPath());
        float points = state.getPoints(file.getCanonicalPath());
        String pointsMessage = "Points : " + points + "/" + current.getMaxPoints();
        String deadLineMessage = checkDeadline(current);
        String submitMessage = "Maximum number of submissions allowed: " + current.getAnswerLimit();
        return new String[] {pointsMessage, deadLineMessage, submitMessage};
    }

    /**
     * Checks if the deadline exists, and changes it into the systems current timezone if it does.
     * @param current the currently open subtask which deadline is being checked.
     * @return a string containing the deadline.
     */
    private String checkDeadline(SubTask current) {
        String deadLineMessage = "no deadline";
        if (current.getDeadLine() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx")
                    .withZone(ZoneId.of("UTC"));
            ZonedDateTime date = ZonedDateTime.parse(current.getDeadLine(), formatter);
            ZoneId localZone = ZoneId.systemDefault();
            ZonedDateTime localDeadline = date.withZoneSameInstant(localZone);
            DateTimeFormatter deadlineFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss z");
            deadLineMessage = localDeadline.format(deadlineFormat);
        }
        return deadLineMessage;
    }
}
