package com.views;

import javax.swing.*;

/**
 * Settings screen.
 */
public class SettingsScreen {
    /**
     * Settings pane.
     */
    private JPanel settings;
    /**
     * Text field for the download path.
     */
    private JTextField cDevelOhj1TextField;
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
     * Gets contents of the settings screen.
     * @return contents.
     */
    public JPanel getContent() {
        return settings;
    }
}
