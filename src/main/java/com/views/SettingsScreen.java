package com.views;

import javax.swing.*;
import java.io.File;

/**
 * Content to be displayed in settings window
 */
public class SettingsScreen {
    private JPanel settings;
    private JTextField pathText;
    private JButton browseButton;
    private JButton OKButton;
    private JButton cancelButton;
    private JPanel path;
    private JPanel buttons;

    /**
     * Constructor to add action listeners for buttons
     */
    public SettingsScreen() {
        this.pathText.setText(com.actions.Settings.getPath());
        this.browseButton.addActionListener(e -> choosePath());
        this.OKButton.addActionListener(e -> updatePath());
        this.cancelButton.addActionListener(e -> com.actions.Settings.close());
    }

    /**
     * Displays a new window where user can choose folder to which demos will be saved
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

    private void updatePath() {
        com.actions.Settings.savePath(this.pathText.getText());
        com.actions.Settings.close();
    }

    /**
     * Return main panel to be displayed
     * @return JPanel containing all the elements
     */
    public JPanel getContent() {
        return settings;
    }
}
