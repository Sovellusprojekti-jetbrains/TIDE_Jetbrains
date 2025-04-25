package com.views;

import com.actions.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Settings screen.
 */
public class SettingsScreen {
    private JPanel settings;
    private JTextField pathText;
    private final int maxScrollSpeed = 1000;

    /**
     * Constructor for the plugin settings screen.
     */
    public SettingsScreen() {
        this.settings = new JPanel(new GridBagLayout());
        int row = 0;
        row = createPathSetting(row);
        createScrollSpeedSetting(row);
    }


    /**
     * Create setting view for TIDE task save path.
     * @param row GridBagLayout row index
     * @return Next row index
     */
    private int createPathSetting(int row) {
        JLabel pathSettingTitle = new JLabel("Task download folder:");
        this.pathText = new JTextField();
        JButton browseButton = new JButton();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.0;
        gbc.gridy = row++;
        gbc.gridx = 0;
        this.settings.add(pathSettingTitle, gbc);
        gbc.weightx = 1.0;
        gbc.gridx = 1;
        this.settings.add(this.pathText, gbc);
        gbc.weightx = 0.0;
        gbc.gridx = 2;
        this.settings.add(browseButton, gbc);
        this.pathText.setText(com.actions.Settings.getPath());
        this.pathText.setToolTipText("Tasks downloaded from the course listing will be saved here");
        browseButton.setText("Browse");
        browseButton.setToolTipText("Use GUI to select download folder");
        browseButton.addActionListener(e -> choosePath());
        return row;
    }


    /**
     * Create setting view for CourseMainPane scroll speed.
     * @param row GridBagLayout row index
     * @return next row index
     */
    private int createScrollSpeedSetting(int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel scrollSpeedLabel = new JLabel("Scroll speed:");
        SpinnerModel model = new SpinnerNumberModel(com.actions.Settings.getScrollSpeed(),
                1,
                maxScrollSpeed,
                1);
        JSpinner scrollSpeedSpinner = new JSpinner(model);
        scrollSpeedSpinner.addChangeListener(e -> Settings.setScrollSpeed((int) scrollSpeedSpinner.getValue()));
        scrollSpeedSpinner.setToolTipText("Set course view scroll speed");
        this.settings.add(scrollSpeedLabel, gbc);
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        this.settings.add(scrollSpeedSpinner, gbc);
        return row;
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
