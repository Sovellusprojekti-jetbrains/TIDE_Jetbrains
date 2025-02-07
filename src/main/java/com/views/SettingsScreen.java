package com.views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Content to be displayed in settings window
 */
public class SettingsScreen {
    private JPanel settings;
    private JTextField cDevelOhj1TextField;
    private JButton browseButton;
    private JButton OKButton;
    private JButton cancelButton;
    private JPanel path;
    private JPanel buttons;


    public SettingsScreen() {
        //TODO: Action listener for ok button to save changes and to close the settings window
        //TODO: Action listener for browse button to call OS file explorer to choose and set file path
        this.cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                com.actions.Settings.close();
            }
        });
    }

    public JPanel getContent() {
        return settings;
    }
}
