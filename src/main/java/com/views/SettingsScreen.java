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
    private JButton OKButton;
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
