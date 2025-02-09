package com.views;

import com.actions.Settings;
import com.api.ApiHandler;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.api.ApiHandler;


public class CourseView {

    private JPanel panel;
    private JPanel coursePanel;
    private JScrollPane coursesPane;
    private JButton logoutButton;
    private JButton settingsButton;
    Color bgColor = new Color(40,40,40);

    public CourseView(){

        panel = new JBPanel<>();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String[] courses = new String[]{"A", "B"};
        for (int i = 0; i < courses.length; i++) {
            JPanel panel1 = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Kurssin nimelle vähän tilaa yläpuolelle
            JPanel labelPanel = new JPanel(new BorderLayout());
            labelPanel.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 0));

            JLabel label = new JLabel();
            label.setText("Course " + courses[i]);
            label.setFont(new Font("Arial", Font.BOLD, 26));
            labelPanel.add(label);
            panel1.add(labelPanel);

            // Random-viikkotehtävät
            // gbc.gridy asettaa ne paikalleen GridBagLayoutissa
            for (int j = 0; j < 10; j++) {
                JPanel subPanel = createExercise(j);
                subPanel.setBackground(bgColor);
                gbc.gridy = j;
                panel1.add(subPanel, gbc);
            }
            panel1.setBackground(bgColor);
            panel1.setOpaque(true);

            // Tehdään scrollpane johon lätkäistään kaikki tähän mennessä tehty.
            JScrollPane scrollPane = new JBScrollPane(panel1);
            scrollPane.setPreferredSize(new Dimension(300, 300)); // Set limited height
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            panel.add(scrollPane);
        }

        settingsButton = new JButton("Settings");

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings temp = new com.actions.Settings();
                temp.displaySettings();
            }
        });
        panel.add(settingsButton);
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApiHandler api = new ApiHandler();
                api.logout();

            }
        });
        panel.add(logoutButton);
    }

    public JPanel getContent() {
        return panel;
    }




    /**
     * Luo rivin viikkotehtävälle nappeineen
     * @param name annettu nimi
     * @return Viikkotehtävärivi
     */
    JPanel createExercise(int name) {
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BorderLayout());
        JLabel labelWeek = new JLabel();
        labelWeek.setText("Label " + name);
        labelWeek.setFont(new Font("Arial", Font.BOLD, 16));
        subPanel.add(labelWeek, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(bgColor);
        subPanel.setBackground(bgColor);

        JButton dButton = new JButton();
        dButton.setText("Download");
        dButton.setBackground(bgColor);
        buttonPanel.add(dButton);

        JButton oButton = new JButton();
        oButton.setText("Open as Project");
        oButton.setBackground(bgColor);
        buttonPanel.add(oButton);

        subPanel.add(buttonPanel, BorderLayout.EAST);

        return subPanel;
    }

}
