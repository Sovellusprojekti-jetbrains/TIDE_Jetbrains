//author Jeremi Kantola
//26.1.2025

package com.views;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.*;

import com.customfile.TimTask;
import com.intellij.util.ui.JBFont;
import com.listeners.SmartLabelRewrapper;
import com.state.ActiveState;
import com.state.StateManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.util.Util;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
* Handles the functional and graphical operations of the right-side task view.
* Taskpane = Main panel containing everything else.
* taskInfoLabel = Label containing the task number from TIDE-CLI.
* taskNameLabel = Label containing the name of the task from TIDE-CLI.
* openTaskButton = Opens a browser window that shows the task description in TIM.
* taskInformationLabel = Nonfunctional label for containing the task description when available from TIM.
* pointsLabel = Label containing the points received from TIDE-CLI.
* submitButton = Submits the task to TIDE-CLI.
* showOutputButton = Shows the output view.
* resetButton = Refetches the task that is open in the editor view from TIDE-CLI.
* submitPane = Contains the points label, submit button and show output buttons.
* infoPane = Contains taskInfo, taskName, openTaskButton, and openTaskButton.
* resetPane = Contains resetButton.
*/
public class CourseTaskPane {
    private JPanel taskPane;
    private JLabel taskInfoLabel;
    private JLabel taskNameLabel;
    private JButton openTaskButton;
    private JLabel taskInformationLabel;
    private JLabel pointsLabel;
    private JButton submitButton;
    private JButton showOutputButton;
    private JButton resetButton;
    private JPanel textPane;
    private JPanel infoPane;
    private JPanel resetPane;
    private JPanel buttonPanel;
    private JProgressBar taskProgressBar;
    private JLabel deadLineLabel;
    private JLabel maxSubmitsLabel;
    private static CourseTaskPane courseTaskPane;
    private List<JLabel> changingLabels = Arrays.asList(taskInformationLabel, taskInfoLabel, taskNameLabel,
            pointsLabel, maxSubmitsLabel, deadLineLabel);
    private ToolWindow thisToolWindow;
    /**
     * getter for the contents of the task panel.
     * @return the task panel
     */
    public JPanel getContent() {
        return taskPane;
    }

    /**
     * A constructor that takes a ToolWindow as a parameter.
     * The Toolwindow instance lets us rewrap labels according
     * to window resizing.
     * @param toolWindow A ToolWindow instance
     */
    public CourseTaskPane(final ToolWindow toolWindow) {
        ActiveState stateManager = ActiveState.getInstance();
        thisToolWindow = toolWindow;

        addActionListeners();

        final int topPadding = 15;
        infoPane.setBorder(BorderFactory.createEmptyBorder(topPadding, 0, 0, 0));
        taskNameLabel.setFont(JBFont.h2());
        final int margin = 20;
        textPane.setBorder(BorderFactory.createEmptyBorder(0, margin, 0, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, margin));
        setButtonTooltips();

        addPropertyChangeListeners(stateManager);

        stateManager.updateCourses();
        setProgress(false, "");
        courseTaskPane = this;
    }

    private void setButtonTooltips() {
        openTaskButton.setToolTipText("Opens the TIM page containing the task either in browser or the editor view.");
        resetButton.setToolTipText("Resets the task to the latest submitted version.");
        submitButton.setToolTipText("Submits the task to TIM.");
        showOutputButton.setToolTipText("Opens the Output panel.");
    }

    private void addPropertyChangeListeners(ActiveState stateManager) {
        stateManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("tideSubmitResponse".equals(evt.getPropertyName())) {
                    String response = (String) evt.getNewValue();
                    handleSubmitResponse(response);
                    setProgress(false, "");
                }
                if ("disableButtons".equals(evt.getPropertyName())) {
                    disableButtons();
                    setDemoName();
                    setDescription("-");
                }
                if ("enableButtons".equals(evt.getPropertyName())) {
                    enableButtons();
                    setDemoName();
                }
                if ("setSubmitData".equals(evt.getPropertyName())) {
                    String[] messages = (String[]) evt.getNewValue();
                    setPoints(messages[0]);
                    setDeadLine(messages[1]);
                    setMaxSubmits(messages[2]);
                    String stem = messages[messages.length - 1];
                    setDescription(Objects.requireNonNullElse(stem, "No description available. To see more, open the task in browser."));
                    SmartLabelRewrapper.setupSmartRewrapForLabels(changingLabels, thisToolWindow);
                }
            }
        });
    }

    private void setDescription(String stem) {
        taskInformationLabel.setText(stem);
    }

    private void addActionListeners() {
        // Open the current exercise in browser
        openTaskButton.addActionListener(event -> {
            ActionManager manager = ActionManager.getInstance();
            AnAction action = manager.getAction("com.actions.BrowserAction");
            manager.tryToExecute(action, null, null, null, true);
        });

        // Reset demo task back to the state of last submit.
        resetButton.addActionListener(event -> {
            ActionManager manager = ActionManager.getInstance();
            AnAction action = manager.getAction("com.actions.ResetExercise");
            manager.tryToExecute(action, null, null, null, true);
        });

        // Submit exercise
        submitButton.addActionListener(event -> {
            ActionManager manager = ActionManager.getInstance();
            AnAction action = manager.getAction("com.actions.Submit");
            manager.tryToExecute(action, null, null, null, true);
        });

        showOutputButton.addActionListener(event -> {
            Util.showWindow(ActiveState.getInstance().getProject(), "Output Window", true);
        });
    }


    /**
     * Deals with a response String received from TIDE-CLI
     * when the user has submitted a file to TIM.
     * @param response from TIDE-CLI
     */
    private void handleSubmitResponse(String response) {
        Project project = ActiveState.getInstance().getProject();
        if (!FileEditorManager.getInstance(project).hasOpenFiles()) {
            InfoView.displayError("Please open a file to submit in the editor.");
            return;
        }

        VirtualFile file = FileEditorManager
                .getInstance(project)
                .getSelectedEditor()
                .getFile();

        String path = file.getPath();
        // The regular expression should match to the "run" and "test"
        // points in TIM output, but not to "Tests run: 1" or similar.
        Pattern pattern = Pattern.compile("(?<=(Points: run: )|(test: ))\\d\\.?(\\d*)?");
        Matcher matcher = pattern.matcher(response);
        List<Float> n = new ArrayList<>();
        if (!response.contains("error")) {
            while (matcher.find()) {
                n.add(Float.parseFloat(String.valueOf(matcher.group())));
            }
        }
        if (n.isEmpty()) {
            n.add(0.0F);
        }
        List<String> submits = ApplicationManager.getApplication().getService(StateManager.class).getSubmits();
        if (submits == null) {
            submits = new ArrayList<>();
        }
        if (!submits.contains(path)
                | ApplicationManager.getApplication().getService(StateManager.class).getPoints(path) != n.get(0)) {
            float nSum = 0;
            for (float f: n) {
                nSum += f;
            }
            ApplicationManager.getApplication().getService(StateManager.class).setSubmit(path, nSum);
        }
        System.out.println(path);
    }

    /**
     * Setter to show the points above the submit button.
     * @param message message containing the points for the submission
     */
    public void setPoints(String message) {
            pointsLabel.setText(message);
    }

    /**
     * setter for the demo task deadline .
     * @param message message containing the deadline.
     */
    private void setDeadLine(String message) {
        deadLineLabel.setText(message);
    }

    /**
     * sets visible number for maximum amount of submissions that is allowed for the demo task.
     * @param message  a message containing the maximum amount of submits allowed.
     */
    private void setMaxSubmits(String message) {
           maxSubmitsLabel.setText(message);
    }

    /**
     * Private method for disabling buttons.
     */
    private void disableButtons() {
        this.openTaskButton.setEnabled(false);
        this.resetButton.setEnabled(false);
        this.submitButton.setEnabled(false);
    }

    /**
     * Private method for enabling buttons.
     */
    private void enableButtons() {
        this.openTaskButton.setEnabled(true);
        this.resetButton.setEnabled(true);
        this.submitButton.setEnabled(true);
    }

    /**
     * Changes the text values of the taskInfoLabel and taskNameLabel.
     */
    private void setDemoName() {
        ApplicationManager.getApplication().invokeLater(() -> {
            if (TimTask.getInstance() != null) {
                String info = TimTask.getInstance().getCourseName() + " - " + TimTask.getInstance().getDemoName();
                this.taskInfoLabel.setText(info);
                this.taskNameLabel.setText(TimTask.getInstance().getDemoTaskName());
            } else {
                this.taskInfoLabel.setText(" Not a TIM task ");
                this.taskNameLabel.setText("");
            }
        });
    }

    /**
     * Sets the progress bars on both tabs of the course panel to the desired visibility and text.
     * @param state Visible, true or false.
     * @param text Text to display on progress bar.
     */
    public void setProgress(boolean state, String text) {
        ApplicationManager.getApplication().invokeLater(() -> {
            taskProgressBar.setString(text);
            taskProgressBar.setVisible(state);
            taskPane.revalidate();
            taskPane.repaint();
        });
    }

    /**
     * Getter for the instance of CourseTaskPane.
     * @return CourseTaskPane.
     */
    public static CourseTaskPane getInstance() {
        return courseTaskPane;
    }
}

