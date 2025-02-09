package com.views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.api.ApiHandler;

public class Loginview {
    private JButton loginButton;
    private JPanel MainPanel;

    public Loginview() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApiHandler api = new ApiHandler();
                api.login();
            }
        });
    }

    public JPanel getContent() {
        return MainPanel;
    }
}
