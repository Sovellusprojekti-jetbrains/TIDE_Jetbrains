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
import java.util.concurrent.atomic.AtomicInteger;


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


        // ilman setLayout-kutsua tämä kaatuu nullpointteriin
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));

        // Courses nyt vaan näin kun JSON-parsimista ei tehdä
        //TODO: pistä hakemaan myös taskit courseList oliosta, tällä hetkellä tehdään erikseen omassa aliohjelmassa, mutta olisi parempi ehkä hakea vain lsita nimistä, jota käsitellään.
        String[] courses = getCourses(courselist);

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
            createTasks(gbc,panel,courseList);
            // Tehdään scrollpane johon lätkäistään kaikki tähän mennessä tehty.
            JScrollPane scrollPane = new JBScrollPane(panel);
            scrollPane.setPreferredSize(new Dimension(300, 300)); // Set limited height
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            coursePanel.add(scrollPane);
        }


        // Piirretään uudelleen
        panel1.revalidate();
        panel1.repaint();
        switchToLogin();


        //currently assumes that the user has the TIM CLI installed
        //need some checks and tests in the future
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
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
                switchToLogin(); // Poistaa kurssinäkymän näkyvistä
                try{
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

    private String[] getCourses(List<Course> courses) {
        String[] names = new String[]{};
        courses.forEach(course->{
            names.add(course.getName());
        });

        return names;
    }

    /**
     * Creates the Tasks for the courses in the course list
     * @param gbc the gridbacconstraint used to put the tasks is their place in the grid bag
     * @param panel The panel that the tasks are put into under the course
     * @param courses the list of courses the student got from the TIDECLI call
     */
    private void createTasks(GridBagConstraints gbc,JPanel panel,List<Course> courses){
        AtomicInteger j = new AtomicInteger();
        courses.foreach(courseTask ->{
            JPanel subPanel = createExercise(courseTask.getName());
            subPanel.setBackground(bgColor);
            gbc.gridy = j.get();
            panel.add(subPanel, gbc);
            panel.setBackground(bgColor);
            panel.setOpaque(true);
            j.getAndIncrement();
        });
    }

    public JPanel getContent() {
        return panel1;
    }

    /**
     * Luo rivin viikkotehtävälle nappeineen
     * @param name annettu nimi
     * @return Viikkotehtävärivi
     */
    JPanel createExercise(String name) {
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
        tabbedPane.addTab("Courses", coursesPane); // Show Logout ta
        tabbedPane.setSelectedComponent(coursesPane);
        //loginButton.setText("Logout");
    }

    private void switchToLogin() {
        tabbedPane.remove(coursesPane); // Hide Courses tab
        //tabbedPane.addTab("Login", loginPane); // Show Login tab
        tabbedPane.setSelectedComponent(loginPane);
    }

}

