//author Jeremi Kantola
//26.1.2025

package com.views;

import java.util.regex.*;
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
* Hoidetaan kaikki ruudun oikealla puolella olevan tehtävän palautuksen suorittavan ikkunan toiminnalliset sekä graaffiset toiminnot
* Taskpane = Kaikki muut paneelit sisältävä pääpaneeli
* demoTiedot = tidecli kautta saadut Demon numero sekä tehtävän numero sisältävä label
* tehtavaNimi = Tidecli kautta saadun tehtävän nimen sisältävä label.
* avaaTehtava = Nappi, jota painamalla tehtävät avautuvat selaimen ikkunaan
* tehtavaTiedot = Tidecli kautta saadun tehtävän tiedot, ei toimi vielä tidecli puolella.
* pisteLabel = Tehtävän palautuksen jälkeen Tidecli antamat pisteet sisältävä label, ei toimi vielä tidecli puolella
* submitButton = nappi, jota painamalla tehtävä lähetetään tidecli kautta TIM-järjestelmälle.
* showOutputButton = nappi, jota painamalla näytetään tidecli lähettämät tiedot terminaalissa.
* resetButton = nappi, jota painamalla tyhjennetään IDE:n tiedosto ja haetaan siihen tehtävän tiedosto uudestaan.
* submitPane = paneeli, joka sisältää pisteLabelin, submitButtonin sekä showOutputButtonin
* infoPane = paneeli, joka sisältää demoTiedot, tehtäväNimi, avaaTehtävä sekä tehtäväTiedot osat.
*  resetPane = paneeli, joka siältää resetButtonin.
*/
public class CourseTaskPane {
    /**
     * The main task pane.
     */
    private JPanel taskPane;
    /**
     * Information of the exercise.
     */
    private JLabel demoTiedot;
    /**
     * Name of the task.
     */
    private JLabel tehtavaNimi;
    /**
     * Button that opens the task.
     */
    private JButton avaaTehtava;
    /**
     * Label for the task info.
     */
    private JLabel tehtavaTiedot;
    /**
     * Points earned from the task.
     */
    private JLabel pisteLabel;
    /**
     * A submit button.
     */
    private JButton submitButton;
    /**
     * Button that shows the console output.
     */
    private JButton showOutputButton;
    /**
     * Button for resetting.
     */
    private JButton resetButton;
    /**
     * Panel for the submission.
     */
    private JPanel submitPane;
    /**
     * Info panel.
     */
    private JPanel infoPane;
    /**
     * Reset panel.
     */
    private JPanel resetPane;
    /**
     * progressbar for ongoing tasks.
     */
    private JProgressBar taskProgressBar;
    /**
     * label for the possible deadline of the subtask.
     */
    private JLabel deadLineLabel;
    /**
     * label for the maximum amount of submissions allowed.
     */
    private JLabel maxSubmitsLabel;
    /**
     * Holds the current project.
     */
    private Project project;

    /**
     * getter for the contents of the task panel.
     * @return the task panel
     */
    public JPanel getContent() {
        return taskPane;
    }


    /**
     * A constructor that takes a ToolWindow as a parameter.
     * The Toolwindow instance lets us access the current project
     * and thus the path of the currently open file.
     * TODO: implement actual functionality somewhere
     * @param toolWindow A ToolWindow instance
     */
    public CourseTaskPane(final ToolWindow toolWindow) {
        this.project = toolWindow.getProject();

        // placeholder for opening the current exercise in browser
        avaaTehtava.addActionListener(event -> {
            ActionManager manager = ActionManager.getInstance();
            AnAction action = manager.getAction("com.actions.BrowserAction");
            manager.tryToExecute(action, null, null, null, true);
        });

        //Resets subtask back to the state of last submit.
        resetButton.addActionListener(event -> {
            ActionManager manager = ActionManager.getInstance();
            AnAction action = manager.getAction("com.actions.ResetExercise");
            manager.tryToExecute(action, null, null, null, true);
        });

        // submit exercise
        submitButton.addActionListener(event -> {
            ActionManager manager = ActionManager.getInstance();
            AnAction action = manager.getAction("com.actions.Submit");
            manager.tryToExecute(action, null, null, null, true);
        });


        showOutputButton.addActionListener(event -> {
            Util.showWindow(project, "Output Window", true);
        });


        ActiveState stateManager = ActiveState.getInstance();
        stateManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("tideSubmitResponse".equals(evt.getPropertyName())) {
                    String response = (String) evt.getNewValue();
                    handleSubmitResponse(response);
                }
                if ("disableButtons".equals(evt.getPropertyName())) {
                    disableButtons();
                }
                if ("enableButtons".equals(evt.getPropertyName())) {
                    enableButtons();
                }
                if ("setSubmitData".equals(evt.getPropertyName())) {
                    String[] messages = (String[]) evt.getNewValue();
                    setPoints(messages[0]);
                    setDeadLine(messages[1]);
                    setMaxSubmits(messages[2]);

                }
                if ("setDemoName".equals(evt.getPropertyName())) {
                    setDemoName((String[]) evt.getNewValue());
                }
                if ("tideSubmitResponse".equals(evt.getPropertyName())) {
                    setProgress(false, "");
                }
            }
        });

        stateManager.updateCourses();
        setProgress(false, "");
    }


    /**
     * Deals with a response String received from TIDE-CLI
     * when the user has submitted a file to TIM.
     * @param response from TIDE-CLI
     */
    private void handleSubmitResponse(String response) {
        if (!FileEditorManager.getInstance(project).hasOpenFiles()) {
            InfoView.displayError("Please open a file to submit in the editor.");
            return;
        }

        VirtualFile file = FileEditorManager
                .getInstance(project)
                .getSelectedEditor()
                .getFile();

        String path = file.getPath();
        Pattern pattern = Pattern.compile("run: \\d+");
        Matcher matcher = pattern.matcher(response);
        List<Float> n = new ArrayList<>();
        if (!response.contains("error")) {
            while (matcher.find()) {
                n.add(Float.parseFloat(String.valueOf(matcher.group().charAt(matcher.group().length() - 1))));
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
            ApplicationManager.getApplication().getService(StateManager.class).setSubmit(path, n.get(0));
        }
        System.out.println(path);
    }

    /**
     * Setter to show the points above the submit button.
     * @param message message containing the points for the submission
     */
    public void setPoints(String message) {
            pisteLabel.setText(message);
    }

    /**
     * setter for the subtask deadline .
     * @param message message containing the deadline.
     */
    private void setDeadLine(String message) {
        deadLineLabel.setText(message);
    }
    /**
     * sets visible number for maximum amount of submissions that is allowed for the subtask.
     * @param message  a message containing the maximum amount of submits allowed.
     */
    private void setMaxSubmits(String message) {
           maxSubmitsLabel.setText(message);
    }

    /**
     * Private method for disabling buttons.
     */
    private void disableButtons() {
        this.avaaTehtava.setEnabled(false);
        this.resetButton.setEnabled(false);
        this.submitButton.setEnabled(false);
    }

    /**
     * Private method for enabling buttons.
     */
    private void enableButtons() {
        this.avaaTehtava.setEnabled(true);
        this.resetButton.setEnabled(true);
        this.submitButton.setEnabled(true);
    }

    /**
     * Changes the text values of the demoTiedot abel and tehtavaNimi label.
     * @param values Values to be set.
     */
    private void setDemoName(String[] values) {
        SwingUtilities.invokeLater(() -> {
            String info = values[0] + " - " + values[1];
            this.demoTiedot.setText(info);
            this.tehtavaNimi.setText(values[2]);
        });
    }

    /**
     * Sets the progress bars on both tabs of the course panel to the desired visibility and text.
     * @param state Visible, true or false.
     * @param text Text to display on progress bar.
     */
    public void setProgress(boolean state, String text) {
        SwingUtilities.invokeLater(() -> {
            taskProgressBar.setString(text);
            taskProgressBar.setVisible(state);
            taskPane.revalidate();
            taskPane.repaint();
        });
    }
}

