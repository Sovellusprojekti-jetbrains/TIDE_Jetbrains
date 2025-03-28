//author Jeremi Kantola
//26.1.2025

package com.views;
import java.util.Objects;
import java.util.regex.*;
import com.actions.ActiveState;
import com.actions.Settings;
import com.actions.StateManager;
import com.api.ApiHandler;
import com.api.JsonHandler;
import com.api.TimDataHandler;
import com.course.SubTask;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                //TODO: The following lines of code up to line 133 repeats in many action listeners.
                // Is there a way to refactor this?
                VirtualFile file = FileEditorManager
                        .getInstance(project)
                        .getSelectedEditor()
                        .getFile();
                try {
                    ActiveState.getInstance().setSubmittable(file);
                } catch (IOException ex) {
                    InfoView.displayError("An error occurred while evaluating if the file is a tim task!");
                    throw new RuntimeException(ex);
                }
                if (!ActiveState.getInstance().isSubmittable()) {
                    InfoView.displayWarning("File in editor is not a tim task!");
                    return;
                }
                try {
                    desktop.browse(new URI("https://timbeta01.tim.education"));
                } catch (IOException | URISyntaxException e) {
                    com.api.LogHandler.logError("111 CourseTaskPane avaaTehtava ActionListener", e);
                    throw new RuntimeException(e);
                }
            }
        });

        //Resets subtask back to the state of last submit.
        resetButton.addActionListener(event -> {
            if (!FileEditorManager.getInstance(project).hasOpenFiles()) {
                com.views.InfoView.displayWarning("No files open in editor!");
                return;
            }

            VirtualFile file = FileEditorManager
                    .getInstance(project)
                    .getSelectedEditor()
                    .getFile();

            // show confirmation dialog and return if the user decides to cancel
            if (com.views.InfoView.displayOkCancelWarning("Confirm reset exercise?", "Reset exercise")) {
                return;
            }
            try {
                ActiveState.getInstance().setSubmittable(file);
            } catch (IOException ex) {
                InfoView.displayError("An error occurred while evaluating if the file is a tim task!");
                throw new RuntimeException(ex);
            }
            if (!ActiveState.getInstance().isSubmittable()) {
                InfoView.displayWarning("File in editor is not a tim task!");
                return;
            }
            ApiHandler handler = new ApiHandler();
            ActiveState stateManager = ActiveState.getInstance();
            String coursePath = stateManager.getCourseName(file.getPath());
            try {
                handler.resetSubTask(file, coursePath);
            } catch (IOException e) {
                com.api.LogHandler.logError("142 CourseTaskPane resetButton ActionListener", e);
                com.api.LogHandler.logDebug(new String[]{"148 VirtualFile file", "169 String coursePath"},
                        new String[]{file.toString(), coursePath});
                InfoView.displayError(".timdata file not found!");
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                com.api.LogHandler.logError("CourseTaskPane resetButton ActionListener", e);
                InfoView.displayError("An error occurred during task reset! Check Tide CLI");
                throw new RuntimeException(e);
            }
        });

        // submit exercise
        submitButton.addActionListener(event -> {
            if (!FileEditorManager.getInstance(project).hasOpenFiles()) {
                printOutput("Please open a file to submit in the editor.");
                return;
            }
            OutputWindow.showWindow(project);

            VirtualFile file = FileEditorManager
                    .getInstance(project)
                    .getSelectedEditor()
                    .getFile();

            // TODO: do something like the following to use the TIDE-CLI
            // function to submit all task files in a directory by checking
            // a checkbox, or find a more sensible way to implement it
            // boolean submitAll = submitAllInDirectoryCheckBox.isSelected();
            // String path = submitAll ? file.getParent().getPath() : file.getPath();

            try {
                ActiveState.getInstance().setSubmittable(file);
            } catch (IOException ex) {
                InfoView.displayError("An error occurred while evaluating if the file is a tim task!");
                throw new RuntimeException(ex);
            }
            if (!ActiveState.getInstance().isSubmittable()) {
                InfoView.displayWarning("File in editor is not a tim task!");
                return;
            }

            new ApiHandler().submitExercise(file);
        });


        showOutputButton.addActionListener(event -> {
            OutputWindow.showWindow(project);
        });


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
                if ("setPoints".equals(evt.getPropertyName())) {
                    setPoints();

                }
                if ("setDemoName".equals(evt.getPropertyName())) {
                    setDemoName((String[]) evt.getNewValue());
                }
            }
        });

        stateManager.updateCourses();
    }


    /**
     * Prints a string to the output toolWindow.
     * @param output String to print
     */
    public void printOutput(String output) {
        OutputWindow.showWindow(project);
        OutputWindow outputWindow = OutputWindow.getInstance();
        if (outputWindow != null) {
            OutputWindow.getInstance().printText(output);
        }
    }


    /**
     * Makes the toolwindow unavailable.
     */
    private void hideWindow() {
        SwingUtilities.invokeLater(() -> {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow window = toolWindowManager.getToolWindow("Course Task");
            if (window != null) {
                window.setAvailable(false);
            }
        });
    }


    /**
     * Makes the toolwindow available.
     */
    private void showWindow() {
        SwingUtilities.invokeLater(() -> {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow window = toolWindowManager.getToolWindow("Course Task");
            if (window != null) {
                window.setAvailable(true);
            }
        });
    }


    /**
     * Deals with a response String received from TIDE-CLI
     * when the user has submitted a file to TIM.
     * @param response from TIDE-CLI
     */
    private void handleSubmitResponse(String response) {
        if (!FileEditorManager.getInstance(project).hasOpenFiles()) {
            printOutput("Please open a file to submit in the editor.");
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
        setPoints();
    }

    /**
     * Setter to show the points above the submit button.
     */
    public void setPoints() {
        StateManager state = new StateManager();
        VirtualFile file = FileEditorManager
                .getInstance(project)
                .getSelectedEditor()
                .getFile();

        float points = state.getPoints(file.getCanonicalPath());
        TimDataHandler tim = new TimDataHandler();
        JsonHandler json = new JsonHandler();
        VirtualFile parentFile = file.getParent();
        String data = "";
        while (data.isEmpty() && !Objects.equals(parentFile.getCanonicalPath(), Settings.getPath())) {
            data = tim.readTimData(parentFile.getCanonicalPath());
            parentFile = parentFile.getParent();
        }
        List<SubTask> sub = json.jsonToSubtask(data);
        float max = 0.0F;
        for (SubTask task : sub) {
                for (String name : task.getFileName()) {
                    if (name.contains(file.getName())
                            && (task.getIdeTaskId().equals(file.getParent().getName()) || name.contains(file.getParent().getName()))) {
                        max = task.getMaxPoints();
                    }
                }
        }
        pisteLabel.setText("Points : " + points + "/" + max);
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
        String info = values[0] + " - " + values[1];
        this.demoTiedot.setText(info);
        this.tehtavaNimi.setText(values[2]);
    }
}

