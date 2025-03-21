package com.views;

import javax.swing.*;
import java.io.File;

/**
 * Settings screen.
 */
public class SettingsScreen {
    /**
     * Settings pane.
     */
    private JPanel settings;
    private JTextField pathText;
    /**
     * Button that opens a file browser.
     */
    private JButton browseButton;
    /**
     * Button that submits the changes.
     */
    private JButton okButton;
    /**
     * Button that cancels the changes.
     */
    private JButton cancelButton;
    /**
     * Panel for the path.
     */
    private JPanel path;
    /**
     * Panel for the ok and cancel buttons.
     */
    private JPanel buttons;

    /**
     * Constructor to add action listeners for buttons.
     */
    public SettingsScreen() {
        this.pathText.setText(com.actions.Settings.getPath());
        this.browseButton.addActionListener(e -> choosePath());
        this.okButton.addActionListener(e -> updatePath());
        this.cancelButton.addActionListener(e -> com.actions.Settings.close());
    }

    /**
     * Displays a new window where user can choose folder to which demos will be saved.
     */
    private void choosePath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        JFrame frame = new JFrame("Choose folder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int value = chooser.showOpenDialog(frame);
        if (value == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = chooser.getSelectedFile();
            this.pathText.setText(selectedFolder.getAbsolutePath());
        }
    }

    /**
     * Displays error message on screen.
     * @param message Error message as String
     * @param title Title for error message
     */
    private void displayError(String message, String title) {
        //com.views.InfoView.displayError(message, title);
    }

    /**
     * Saves path to persistent state component.
     */
    private void updatePath() {
        File tempFile = new File(this.pathText.getText());
        if (tempFile.exists()) {
            com.actions.Settings.savePath(this.pathText.getText());
            com.actions.Settings.close();
        } else {
            displayError("Directory doesn't exist!", "Path error");
        }
    }

    /**
     * Return main panel to be displayed.
     * @return JPanel containing all the elements
     */
    public JPanel getContent() {
        return settings;
    }

    /**
     * Removes ok and cancel buttons when this view is used inside idea settings.
     */
    public void noButtons() {
        this.buttons.remove(this.okButton);
        this.buttons.remove(this.cancelButton);
    }

    /**
     * This method is needed to check changes in the method in AppSettingsConfigurable.
     * @return text in the text field
     */
    public String getPathText() {
        return this.pathText.getText();
    }

    /**
     * This method is needed to revert changes in the idea settings.
     * @param text valid path to set into the pathText text field
     */
    public void setPathText(String text) {
        this.pathText.setText(text);
    }
}
