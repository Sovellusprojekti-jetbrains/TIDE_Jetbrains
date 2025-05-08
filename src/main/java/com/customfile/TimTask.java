package com.customfile;

import com.api.ApiHandler;
import com.api.LogHandler;
import com.course.SubTask;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.interfaces.TideTask;
import com.state.ActiveState;
import com.state.StateManager;
import com.util.Util;
import com.views.CourseTaskPane;
import com.views.InfoView;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * This class "extends" VirtualFile with actions of tide task.
 */
public final class TimTask implements TideTask {

    private static TimTask selected; //Keep the instance of TimTask opened in the editor here.
    private final VirtualFile delegate;
    private final ArrayList<String> headers;
    private final SubTask task;
    private static final HashMap<String, TimTask> CACHE = new HashMap<>();

    /**
     * In order to "extend" VirtualFile we must have one as a delegate.
     * @param file VirtualFile which is used as a delegate.
     * @param headerList Contains course name, demo name, and subtask name.
     * @param subTask Object reference to SubTask object containing all the goods.
     */
    private TimTask(VirtualFile file, ArrayList<String> headerList, SubTask subTask) {
        this.delegate = file;
        this.headers = headerList;
        this.task = subTask;
    }

    /**
     * This method submits exercise to tim.
     * @param project project that is open in the ide.
     */
    public void submit(Project project) {
        Util.showWindow(project, "Output Window", true);
        try {
            CourseTaskPane.getInstance().setProgress(true, "Submitting...");
        } catch (Exception ex) {
            LogHandler.logError("TimTask.submit(Project project)", ex);
        }
        this.syncChanges();
        new ApiHandler().submitExercise(this.delegate);
    }

    /**
     * This method resets the exercise back to the state of the latest submit.
     */
    public void resetExercise() {
        this.syncChanges();
        try {
            new ApiHandler().resetSubTask(this.task, this.headers.get(0));
        } catch (IOException ex) {
            com.api.LogHandler.logError("TimTask.resetExercise()", ex);
            InfoView.displayError(".timdata file not found!");
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            com.api.LogHandler.logError("TimTask.resetExercise()", ex);
            InfoView.displayError("An error occurred during task reset! Check Tide CLI");
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method is used to show the exercise's tim-page in browser.
     * @param baseURL URL for tim.
     * @param project project that is open in the ide.
     */
    public void openInBrowser(String baseURL, Project project) {
        StateManager state = Objects.requireNonNull(ApplicationManager.getApplication().getService(StateManager.class));
        String url = baseURL;
        //TODO: handle null case.
        url += this.task.getPath();
        //the task name needed for the url is not part of the subtask but part of the task file
        //we need to get the first file of the task to get the right id.
        //id is in form number.name.idstring
        //thus we split the id to get the relevant part in the middle.
        url += "#" + this.task.getTaskFiles().get(0).getTaskIdExt().split("\\.")[1];

        if (state.getBrowserChoice()) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (URISyntaxException | IOException e) {
                    InfoView.displayError(e.getMessage());
                    LogHandler.logError("TimTask.OpenInBrowser()", e);
                }
            }
        } else {
            // Set the website URL
            HtmlEditorProvider.setUrl(url);

            // Create a virtual file with the expected name
            VirtualFile file = new LightVirtualFile("website_view"); // Must match HtmlEditorProvider's `accept()`

            // Open the file in the editor
            FileEditorManager.getInstance(project).openFile(file, true);
        }
    }

    /**
     * This method updates the changes in the delegate VirtualFile to the physical file on disk.
     */
    private void syncChanges() {
        FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
        Document document = fileDocumentManager.getDocument(this.delegate);
        if (document != null) {
            fileDocumentManager.saveDocument(document);
        }
    }

    /**
     * This method is used to update the SubTask information into CourseTaskPane, and to update it's state etc.
     */
    private static void messageUpdates() {
        ActiveState.getInstance().setSubmittable(selected != null);
        ActiveState.getInstance().messageChanges();
    }

    /**
     * Getter for active instance.
     * @return TimTask.
     */
    public static TimTask getInstance() {
        return selected;
    }

    /**
     * Returns Course name.
     * @return Course name as String.
     */
    public String getCourseName() {
        return this.headers.get(0);
    }

    /**
     * Returns Demo name.
     * @return Demo name as String.
     */
    public String getDemoName() {
        return this.headers.get(1);
    }

    /**
     * Returns SubTask name.
     * @return SubTask name as String.
     */
    public String getSubTaskName() {
        return this.headers.get(this.headers.size() - 1);
    }

    /**
     * Getter for SuTask's submit data.
     * @return string array of messages.
     */
    public String[] getSubmitData() {
        StateManager state = new StateManager();
        float points = state.getPoints(this.delegate.getCanonicalPath());
        String pointsMessage = "<html><b>Points :</b> " + points + "/" + this.task.getMaxPoints() + "</html>";
        String deadLineMessage = this.getDeadline();
        int answerLimit = this.task.getAnswerLimit();
        // TODO: Ideally, there would be a distinction between no tries left and tries left not available.
        String answerLimitString = answerLimit > 0 ? String.valueOf(answerLimit) : "N/A";
        String submitMessage = "<html><b>Tries left: </b>" + answerLimitString + "</html>";
        return new String[] {pointsMessage, deadLineMessage, submitMessage};
    }

    /**
     * Checks if the deadline exists, and changes it into the systems current timezone if it does.
     * @return a string containing the deadline.
     */
    private String getDeadline() {
        String deadLineMessage = "<html><b>Deadline:</b> N/A</html>";
        if (this.task.getDeadLine() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx")
                    .withZone(ZoneId.of("UTC"));
            ZonedDateTime date = ZonedDateTime.parse(this.task.getDeadLine(), formatter);
            ZoneId localZone = ZoneId.systemDefault();
            ZonedDateTime localDeadline = date.withZoneSameInstant(localZone);
            DateTimeFormatter deadlineFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss z");
            deadLineMessage = "<html><b>Deadline: </b>" + localDeadline.format(deadlineFormat) + "</html>";
        }
        return deadLineMessage;
    }

    /**
     * This method is called from ActiveState when MessageBus event fires.
     * @param file VirtualFile opened in the editor which must be evaluated.
     */
    public static void evaluateFile(VirtualFile file) {
        if (file != null && file.getCanonicalPath() != null) {
            if (CACHE.containsKey(file.getUrl())) {
                selected = CACHE.get(file.getUrl());
            } else {
                ArrayList<String> taskHeaders = new ArrayList<>();
                taskHeaders.add(ActiveState.getInstance().getCourseName(file.getPath()));
                taskHeaders.add(ActiveState.getInstance().findTaskName(taskHeaders.get(0), file));
                SubTask taskHolder = ActiveState.getInstance().findSubTask(file);
                if (taskHolder != null) {
                    taskHeaders.add(taskHolder.getIdeTaskId());
                    selected = new TimTask(file, taskHeaders, taskHolder);
                    CACHE.put(file.getUrl(), selected);
                } else {
                    selected = null;
                    CACHE.put(file.getUrl(), selected);
                }
            }
        } else {
            selected = null;
        }
        messageUpdates();
    }
}
