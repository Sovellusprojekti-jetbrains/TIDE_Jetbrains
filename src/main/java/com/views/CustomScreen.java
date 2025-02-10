package com.views;

import com.actions.Settings;
import com.intellij.ui.components.JBScrollPane;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class for displaying a template course window.
 */
public class CustomScreen {
    /**
     * The button that does the login action.
     */
    private JButton loginButton;
    /**
     * The main pane for the window.
     */
    private JPanel panel1;
    /**
     * The pane that contains all the tabs for the window.
     */
    private JTabbedPane tabbedPane;
    /**
     * The pane containing the login and settings buttons.
     */
    private JPanel loginPane;
    // private JPanel titlePanel;
    // private JLabel courseLabel;
    /**
     * Course panel contains all the courses and their exercises.
     */
    private JPanel coursePanel;
    /**
     * Courses pane should be turned into a normal pane instead of a scrollPane.
     */
    private JScrollPane coursesPane;
    /**
     * Button for logging out.
     */
    private JButton logoutButton;
    /**
     * Button that opens the settings window.
     */
    private JButton settingsButton;
    private final int red = 40;
    private final int green = 40;
    private final int blue = 40;
    private final Color bgColor = new Color(red, green, blue);

    /**
     * Constructor for CustomScreen.
     */
    public CustomScreen() {


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
            final int top = 20;
            final int left = 5;
            final int bottom = 5;
            final int right = 0;
            labelPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));

            final int FONTSIZE = 26;
            JLabel label = new JLabel();
            label.setText("Course " + courses[i]);
            label.setFont(new Font("Arial", Font.BOLD, FONTSIZE));
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

            final int paneSize = 300;
            final int thickness = 4;

            // Tehdään scrollpane johon lätkäistään kaikki tähän mennessä tehty.
            JScrollPane scrollPane = new JBScrollPane(panel);
            scrollPane.setPreferredSize(new Dimension(paneSize, paneSize)); // Set limited height
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, thickness));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            coursePanel.add(scrollPane);
        }


        // Piirretään uudelleen
        panel1.revalidate();
        panel1.repaint();
        switchToLogin();

         // currently assumes that the user has the TIM CLI installed
         // need some checks and tests in the future.
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String command = "tide login";

                    ProcessBuilder pb = new ProcessBuilder(command.split("\\s+"));
                    pb.redirectErrorStream(true);
                    Process process = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                    int exitCode = process.waitFor();
                    System.out.println("Process exited with code: " + exitCode);

                    switchToLogout(); // Poistaa loginin näkyvistä
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                    switchToLogout();
                }
            }
        });

        // Adds an action listener for the settings button
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                Settings temp = new com.actions.Settings();
                temp.displaySettings();
            }
        });


        // Adds an action listener for the logout button.
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                switchToLogin(); // Poistaa kurssinäkymän näkyvistä
                try {
                    String command = "tide logout";

                    ProcessBuilder pb = new ProcessBuilder(command.split("\\s+"));
                    pb.redirectErrorStream(true);
                    Process process = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                    int exitCode = process.waitFor();
                    System.out.println("Process exited with code: " + exitCode);


                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        );

    }

    /**
     * Gets the content of the panel.
     * @return main panel
     */
    public JPanel getContent() {
        return panel1;
    }

    /**
     * Luo rivin viikkotehtävälle nappeineen.
     * @param name annettu nimi
     * @return Viikkotehtävärivi
     */
    JPanel createExercise(int name) {
        final int fontSize = 16;
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BorderLayout());
        JLabel labelWeek = new JLabel();
        labelWeek.setText("Label " + name);
        labelWeek.setFont(new Font("Arial", Font.BOLD, fontSize));
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

    /**
     * Switches to a state where logging out is possible.
     */
    private void switchToLogout() {
        //tabbedPane.remove(loginPane); // Hide Login tab
        tabbedPane.addTab("Courses", coursesPane); // Show Logout ta
        tabbedPane.setSelectedComponent(coursesPane);
        //loginButton.setText("Logout");
    }

    /**
     * Switches to a state where logging in is possible.
     */
    private void switchToLogin() {
        tabbedPane.remove(coursesPane); // Hide Courses tab
        //tabbedPane.addTab("Login", loginPane); // Show Login tab
        tabbedPane.setSelectedComponent(loginPane);
    }

}

