package com.views;

import com.actions.Settings;
import com.api.ApiHandler;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;




public class CustomScreen {
    private JButton loginButton;
    private JPanel panel1;
    private JTabbedPane tabbedPane;
    private JPanel loginPane;
    private JPanel titlePanel;
    private JLabel courseLabel;
    private JPanel coursePanel;
    private JScrollPane coursesPane;
    private JButton logoutButton;
    private JButton settingsButton;
    Color bgColor = new Color(40,40,40);


    public CustomScreen() {

        //Content loginContent = contentFactory.createContent(loginPane, "Login", false);
        //Content logoutContent = contentFactory.createContent(coursesPane, "Content", false);
        // Piirretään uudelleen
        //panel1.revalidate();
        //panel1.repaint();
        ApiHandler api = new ApiHandler();





        //currently assumes that the user has the TIM CLI installed
        //need some checks and tests in the future
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApiHandler api = new ApiHandler();
                api.login();
                if(api.isLoggedIn()){
                    switchToLogout();
                } else {
                    //TODO: error message that the login failed
                    return;
                }


            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings temp = new com.actions.Settings();
                temp.displaySettings();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApiHandler api = new ApiHandler();
                api.logout();
                if(!api.isLoggedIn()){
                    switchToLogin(); // Poistaa kurssinäkymän näkyvistä
                }else {
                    //TODO: error for failed logout
                    return;
                }


            }
        }
        );
        //tabbedPane.addTab("Login", loginPane);
        if(api.isLoggedIn()) {
            switchToLogout();
        } else {
            switchToLogin();
        }




    }

    public JPanel getContent() {
        return panel1;
    }

    private void createCourseView() {
        // ilman setLayout-kutsua tämä kaatuu nullpointteriin
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));

        // Kurssit nyt vaan näin kun JSON-parsimista ei tehdä
        String[] courses = new String[]{"A", "B"};

        for (int i = 0; i < courses.length; i++) {
            JPanel panel = new JPanel(new GridBagLayout());
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
            coursePanel.add(labelPanel);

            // Random-viikkotehtävät
            // gbc.gridy asettaa ne paikalleen GridBagLayoutissa
            for (int j = 0; j < 10; j++) {
                JPanel subPanel = createExercise(j);
                subPanel.setBackground(bgColor);
                gbc.gridy = j;
                panel.add(subPanel, gbc);
            }
            panel.setBackground(bgColor);
            panel.setOpaque(true);

            // Tehdään scrollpane johon lätkäistään kaikki tähän mennessä tehty.
            JScrollPane scrollPane = new JBScrollPane(panel);
            scrollPane.setPreferredSize(new Dimension(300, 300)); // Set limited height
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            coursePanel.add(scrollPane);
        }


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

    private void switchToLogout() {
        //tabbedPane.remove(loginPane); // Hide Login tab
        createCourseView();
        tabbedPane.addTab("Courses", coursesPane); // Show Logout tab
        loginButton.setEnabled(false);
        panel1.revalidate();
        panel1.repaint();
        tabbedPane.setSelectedComponent(coursesPane);

        //loginButton.setText("Logout");
    }

    private void switchToLogin() {
        tabbedPane.remove(coursesPane); // Hide Courses tab
        tabbedPane.addTab("Login", loginPane); // Show Login tab
        loginButton.setEnabled(true);
        panel1.revalidate();
        panel1.repaint();
        tabbedPane.setSelectedComponent(loginPane);
    }

}

