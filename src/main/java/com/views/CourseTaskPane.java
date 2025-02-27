//author Jeremi Kantola
//26.1.2025

package com.views;

import com.api.JsonHandler;
import com.course.SubTask;
import com.course.Course;
import com.api.ApiHandler;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindowManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

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
                try {
                    desktop.browse(new URI("https://timbeta01.tim.education"));
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // placeholder for resetting exercises;
        // should behave similar to com.actions.Reset, so both
        // can delegate processing the path to a third party
        resetButton.addActionListener(event -> {
            String path = Objects.requireNonNull(FileEditorManager
                    .getInstance(project)
                    .getSelectedEditor())
                    .getFile()
                    .getPath();

            // show confirmation dialog and return
            // if the user decides to cancel
            if (com.views.InfoView.displayOkCancelWarning("Confirm reset exercise?", "Reset exercise")) {
                return;
            }

            // kutsu tehtävänlataajaa vivulla -f
            System.out.println(path);
            try {
                String timdata = com.actions.Settings.getPath() + "/.timdata";
                System.out.println(timdata);
                String taskdata = Files.readString(Path.of(timdata), StandardCharsets.UTF_8);
                System.out.println(taskdata);
                JsonHandler handler = new JsonHandler();
                List<SubTask> subtasks = handler.jsonToSubtask(taskdata);
                System.out.println(subtasks);
                //TODO: Etsi oikea taskId tiedostonimen perusteella
                //TODO: Kutsu ApiHandlerin load exercise metodia vivulla -f
                //TODO: mahd. virheiden/poikkeuksien käsitteleminen
            } catch (IOException e) {
                InfoView.displayError(".timdata file not found!", "Task reset error");
                throw new RuntimeException(e);
            }
        });

        // submit exercise
        submitButton.addActionListener(event -> {
            if (!FileEditorManager.getInstance(project).hasOpenFiles()) {
                printOutput("Please open a file to submit in the editor.");
                return;
            }

            VirtualFile file = FileEditorManager
                    .getInstance(project)
                    .getSelectedEditor()
                    .getFile();

            String path = file.getPath();
            // TODO: do something like the following to use the TIDE-CLI
            // function to submit all task files in a directory by checking
            // a checkbox, or find a more sensible way to implement it
            // boolean submitAll = submitAllInDirectoryCheckBox.isSelected();
            // String path = submitAll ? file.getParent().getPath() : file.getPath();

            String response = new ApiHandler().submitExercise(path);
            printOutput(response);
            System.out.println(path);
        });


        showOutputButton.addActionListener(event -> {
            printOutput("TimBetan tehtävät palauttaa vaan yhden rivin virheen.\n"
                    + "Tässä siis jotain mallitekstiä, kun merkkijonoja sieltä timistäkin vaan tulee."); //TODO: poista
        });

    }


    /**
     * Prints a string to the output toolWindow.
     * @param output String to print
     */
    public void printOutput(String output) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow window = toolWindowManager.getToolWindow("Output Window");

        if (window != null) {
            window.show(null);
            OutputWindow.getInstance().printText(output);
        }
    }
}

