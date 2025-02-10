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

import com.course.*;
import com.api.JsonHandler;

import java.util.List;


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
    private JButton refreshButton;
    Color bgColor = new Color(40,40,40);


    public CustomScreen() {

        /**
         * Json data that correctly maps to Course objects.
         * The format is an array of Json objects.
         * This is for testing, since we cant download actual json data yet
         */
        String validJsonData = "[\n"
                + "  {\n"
                + "      \"name\": \"ITKP101, ohjelmointi 1\",\n"
                + "      \"id\": 11203,\n"
                + "      \"path\": \"kurssit/tie/ohj1/2025k/demot\",\n"
                + "      \"tasks\": [\n"
                + "          {\n"
                + "              \"name\": \"Demo1\",\n"
                + "              \"doc_id\": 401648,\n"
                + "              \"path\": \"kurssit/tie/ohj1/2025k/demot/Demo1\"\n"
                + "          },\n"
                + "          {\n"
                + "              \"name\": \"Demo2\",\n"
                + "              \"doc_id\": 401649,\n"
                + "              \"path\": \"kurssit/tie/ohj1/2025k/demot/Demo2\"\n"
                + "          },\n"
                + "          {\n"
                + "            \"name\": \"Demo3\",\n"
                + "            \"doc_id\": 401650,\n"
                + "            \"path\": \"kurssit/tie/ohj1/2025k/demot/Demo3\"\n"
                + "        }\n"
                + "      ]\n"
                + "      \n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"ITKP102, ohjelmointi 2\",\n"
                + "    \"id\": 16103,\n"
                + "    \"path\": \"kurssit/tie/ohj2/2025k/demot\",\n"
                + "    \"tasks\": [\n"
                + "        {\n"
                + "            \"name\": \"Demo1\",\n"
                + "            \"doc_id\": 501370,\n"
                + "            \"path\": \"kurssit/tie/ohj2/2025k/demot/Demo1\"\n"
                + "        },\n"
                + "        {\n"
                + "            \"name\": \"Demo2\",\n"
                + "            \"doc_id\":  501372,\n"
                + "            \"path\": \"kurssit/tie/ohj2/2025k/demot/Demo2\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"name\": \"Demo3\",\n"
                + "          \"doc_id\":  501374,\n"
                + "          \"path\": \"kurssit/tie/ohj2/2025k/demot/Demo3\"\n"
                + "      }\n"
                + "    ]\n"
                + "    }\n"
                +
                "]";
        //Content loginContent = contentFactory.createContent(loginPane, "Login", false);
        //Content logoutContent = contentFactory.createContent(coursesPane, "Content", false);

        JsonHandler handler = new JsonHandler();
        // ilman setLayout-kutsua tämä kaatuu nullpointteriin
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));

        List<Course> courselist = handler.jsonToCourses(validJsonData);
        createCourseListPane(courselist);

        // Piirretään uudelleen
        panel1.revalidate();
        panel1.repaint();
        switchToLogin();

        // Gets the demos and tasks from tidecli again
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String validJsonData2 = "[\n"
                        + "  {\n"
                        + "      \"name\": \"ITKP101, ohjelmointi 1\",\n"
                        + "      \"id\": 11203,\n"
                        + "      \"path\": \"kurssit/tie/ohj1/2025k/demot\",\n"
                        + "      \"tasks\": [\n"
                        + "          {\n"
                        + "              \"name\": \"Demo1\",\n"
                        + "              \"doc_id\": 401648,\n"
                        + "              \"path\": \"kurssit/tie/ohj1/2025k/demot/Demo1\"\n"
                        + "          },\n"
                        + "          {\n"
                        + "              \"name\": \"Demo2\",\n"
                        + "              \"doc_id\": 401649,\n"
                        + "              \"path\": \"kurssit/tie/ohj1/2025k/demot/Demo2\"\n"
                        + "          },\n"
                        + "          {\n"
                        + "            \"name\": \"Demo3\",\n"
                        + "            \"doc_id\": 401650,\n"
                        + "            \"path\": \"kurssit/tie/ohj1/2025k/demot/Demo3\"\n"
                        + "        },\n"
                        + "          {\n"
                        + "            \"name\": \"Demo4\",\n"
                        + "            \"doc_id\": 401654,\n"
                        + "            \"path\": \"kurssit/tie/ohj1/2025k/demot/Demo4\"\n"
                        + "        }\n"
                        + "      ]\n"
                        + "      \n"
                        + "  },\n"
                        + "  {\n"
                        + "    \"name\": \"ITKP102, ohjelmointi 2\",\n"
                        + "    \"id\": 16103,\n"
                        + "    \"path\": \"kurssit/tie/ohj2/2025k/demot\",\n"
                        + "    \"tasks\": [\n"
                        + "        {\n"
                        + "            \"name\": \"Demo1\",\n"
                        + "            \"doc_id\": 501370,\n"
                        + "            \"path\": \"kurssit/tie/ohj2/2025k/demot/Demo1\"\n"
                        + "        },\n"
                        + "        {\n"
                        + "            \"name\": \"Demo2\",\n"
                        + "            \"doc_id\":  501372,\n"
                        + "            \"path\": \"kurssit/tie/ohj2/2025k/demot/Demo2\"\n"
                        + "        },\n"
                        + "        {\n"
                        + "          \"name\": \"Demo3\",\n"
                        + "          \"doc_id\":  501374,\n"
                        + "          \"path\": \"kurssit/tie/ohj2/2025k/demot/Demo3\"\n"
                        + "      }\n"
                        + "    ]\n"
                        + "    }\n"
                        +
                        "]";
                JsonHandler handler = new JsonHandler();
                List<Course> refreshed = handler.jsonToCourses(validJsonData2);
                createCourseListPane(refreshed);

                // Piirretään uudelleen
                panel1.revalidate();
                panel1.repaint();
            }


        });
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

    private void createCourseListPane(List<Course> courselist) {
        coursePanel.removeAll();
        courselist.forEach(Course ->{
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Kurssin nimelle vähän tilaa yläpuolelle
            JPanel labelPanel = new JPanel(new BorderLayout());
            labelPanel.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 0));

            JLabel label = new JLabel();
            label.setText("Course " + Course.getName());
            label.setFont(new Font("Arial", Font.BOLD, 26));
            labelPanel.add(label);
            coursePanel.add(labelPanel);

            // Makes own subpanel for every task
            // gbc.gridy asettaa ne paikalleen GridBagLayoutissa
            List<CourseTask>tasks = Course.getTasks();
            final int[] j = {0};
            tasks.forEach( CourseTask ->{
                JPanel subPanel = createExercise(CourseTask.getName(), CourseTask.getPath());
                subPanel.setBackground(bgColor);
                gbc.gridy = j[0];
                panel.add(subPanel, gbc);
                panel.setBackground(bgColor);
                panel.setOpaque(true);
                j[0]++;
            });
            // Tehdään scrollpane johon lätkäistään kaikki tähän mennessä tehty.
            JScrollPane scrollPane = new JBScrollPane(panel);
            scrollPane.setPreferredSize(new Dimension(300, 300)); // Set limited height
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            coursePanel.add(scrollPane);
        });

    }


    public JPanel getContent() {
        return panel1;
    }

    /**
     * Creates a panel for the task together with the buttons to download or open it.
     * @param name  the name of the task
     * @param path path used to download the demo
     * @return the subpanel that contains the tasks name and the two buttons
     */
    JPanel createExercise(String name,String path) {
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BorderLayout());
        JLabel labelWeek = new JLabel();
        labelWeek.setText(name);
        labelWeek.setFont(new Font("Arial", Font.BOLD, 16));
        subPanel.add(labelWeek, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(bgColor);
        subPanel.setBackground(bgColor);

        JButton dButton = new JButton();
        dButton.setText("Download");
        dButton.setBackground(bgColor);
        dButton.addActionListener(new ActionListener() {
            @Override
            //TODO:muuta kutsumaan aliohjelmaa, joka lataa tiedoston koneelle.
            public void actionPerformed(ActionEvent e) {
                System.out.println(path);
                                      }
                                  });
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

