package com.views;

import com.intellij.openapi.application.ApplicationManager;
import com.state.StateManager;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.io.File;
import java.util.Objects;

/**
 * Settings screen.
 */
public class SettingsScreen {
    private JPanel settings;
    private JTextField pathText;
    private JSpinner scrollSpeedSpinner;
    private JTextField tidePathText;

    /**
     * Constructor for the plugin settings screen.
     */
    public SettingsScreen() {
        this.settings = new JPanel(new GridBagLayout());
        int row = 0;
        row = createPathSetting(row);
        row = createScrollSpeedSetting(row);
        row = tidePathSetting(row);
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
        // Explicitly setting the column number for the JTextField prevents the
        // SettingsScreen from expanding beyond the size of the containing element
        // and creating an unwanted horizontal scrollbar when the JTextField content
        // is too long for the viewport. The value can be arbitrary as long as it is
        // small enough. The GridBagConstraints properties make the text field expand
        // to fill the settings view, but no further.
        final int pathColumns = 10;
        pathText.setColumns(pathColumns);
        this.settings.add(this.pathText, gbc);

        gbc.weightx = 0.0;
        gbc.gridx = 2;
        this.settings.add(browseButton, gbc);
        this.pathText.setText(com.actions.Settings.getPath());
        this.pathText.setToolTipText("Tasks downloaded from the course listing will be saved here");
        browseButton.setText("Browse");
        browseButton.setToolTipText("Use GUI to select download folder");
        browseButton.addActionListener(e -> choosePath(this.pathText));
        return row;
    }


    /**
     * Create setting view for CourseMainPane scroll speed.
     * @param row GridBagLayout row index
     * @return Next row index
     */
    private int createScrollSpeedSetting(int row) {
        StateManager state = Objects.requireNonNull(ApplicationManager.getApplication().getService(StateManager.class));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel scrollSpeedLabel = new JLabel("Scroll speed:");
        SpinnerModel model = new SpinnerNumberModel(com.actions.Settings.getScrollSpeed(),
                1,
                state.getMaxScrollSpeed(),
                1);
        scrollSpeedSpinner = new JSpinner(model);

        // spinner default behavior is to commit the value on loss of focus, so we need to do
        // the following for the apply button in the settings screen to enable on spinner value changes
        DefaultFormatter formatter = (DefaultFormatter) ((JFormattedTextField) scrollSpeedSpinner.getEditor()
                .getComponent(0))
                .getFormatter();
        formatter.setCommitsOnValidEdit(true);

        scrollSpeedSpinner.setToolTipText("Set course view scroll speed");
        this.settings.add(scrollSpeedLabel, gbc);
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        this.settings.add(scrollSpeedSpinner, gbc);
        return row;
    }


    /**
     * Displays a new window where user can choose folder to which demos will be saved.
     * @param field the text field that is being worked on
     */
    private void choosePath(JTextField field) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        JFrame frame = new JFrame("Choose folder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int value = chooser.showOpenDialog(frame);
        if (value == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = chooser.getSelectedFile();
            field.setText(selectedFolder.getAbsolutePath());
        }
    }

    /**
     * @param row GridBagLayout row index
     * @return Next row index
     */
    private int tidePathSetting(int row) {
        JLabel tideSettingTitle = new JLabel("Tide install folder:");
        this.tidePathText = new JTextField();
        JButton browseButton = new JButton();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.0;
        gbc.gridy = row++;
        gbc.gridx = 0;
        this.settings.add(tideSettingTitle, gbc);
        gbc.weightx = 1.0;
        gbc.gridx = 1;
        // Explicitly setting the column number for the JTextField prevents the
        // SettingsScreen from expanding beyond the size of the containing element
        // and creating an unwanted horizontal scrollbar when the JTextField content
        // is too long for the viewport. The value can be arbitrary as long as it is
        // small enough. The GridBagConstraints properties make the text field expand
        // to fill the settings view, but no further.
        final int pathColumns = 10;
        tidePathText.setColumns(pathColumns);
        this.settings.add(this.tidePathText, gbc);
        this.tidePathText.setText(com.actions.Settings.getTidePath());
        this.tidePathText.setToolTipText("select where tide is installed leave empty to use computer path instead");
        gbc.weightx = 0.0;
        gbc.gridx = 2;
        this.settings.add(browseButton, gbc);
        browseButton.setText("Browse");
        browseButton.setToolTipText("Use GUI to select tide install folder");
        browseButton.addActionListener(e -> choosePath(this.tidePathText));
        return row;
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
     * @return Value from spinner element
     */
    public int getScrollSpeedSpinnerValue() {
        return (int) this.scrollSpeedSpinner.getValue();
    }


    /**
     * This method is needed to revert changes in the idea settings.
     * @param val Value to set for spinner
     */
    public void setScrollSpeedSpinnerValue(int val) {
        this.scrollSpeedSpinner.setValue(val);
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


    /**
     * This method is needed to check changes in the method in AppSettingsConfigurable.
     * @return text in the text field
     */
    public String getTidePath() {
        return this.tidePathText.getText();
    }
    /**
     * This method is needed to revert changes in the idea settings.
     * @param text valid path to set into the pathText text field
     */
    public void setTidePathText(String text) {
        this.tidePathText.setText(text);
    }
}
