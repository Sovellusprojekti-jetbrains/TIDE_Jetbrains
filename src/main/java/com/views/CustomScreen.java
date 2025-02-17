package com.views;

import com.actions.Settings;
import com.api.ApiHandler;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


import com.course.*;
import java.util.List;

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
    /**
     * TitlePanel seems to exists for a test.
     */
    private JPanel titlePanel;
    /**
     * Label for the course. Investigate usefulness.
     */
    private JLabel courseLabel;
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
    /**
     * Button that refreshes the available courses and tasks.
     */
    private JButton refreshButton;

    /**
     * An integer for the red band of a color.
     */
    private final int red = 40;
    /**
     * The green band of an RGB color.
     */
    private final int green = 40;
    /**
     * The blue band of an RGB color.
     */
    private final int blue = 40;
    /**
     * A color definition.
     */
    private final Color bgColor = new Color(red, green, blue);

    /**
     * Creator for the CustomScreen class, that holds the courses and tasks.
     */
    public CustomScreen() {
        // ilman setLayout-kutsua tämä kaatuu nullpointteriin
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
        ApiHandler apiHandler = new ApiHandler();
        // Fetching data from TIM and creating a list of course objects,
        // for more information see package com.course and class ApiHandler.


        // Piirretään uudelleen
        //panel1.revalidate();
        //panel1.repaint();
        //switchToLogin();

        // needs tests in the future.
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Course> refreshed = apiHandler.courses();
                createCourseListPane(refreshed);

                // Piirretään uudelleen
                panel1.revalidate();
                panel1.repaint();
            }


        });
        //currently assumes that the user has the TIM CLI installed.
        //need some checks and tests in the future.
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApiHandler api = new ApiHandler();
                try {
                    api.login();
                    if (api.isLoggedIn()) {
                        switchToLogout();
                    } else {
                        //TODO: error message that the login failed
                        return;
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        // Adds an action listener for the settings button.
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings temp = new com.actions.Settings();
                temp.displaySettings();
            }
        });

        // Adds an action listener for the logout button.
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApiHandler api = new ApiHandler();
                //switchToLogin(); // Poistaa kurssinäkymän näkyvistä
                try {
                    api.logout();
                    if (!api.isLoggedIn()) {
                        switchToLogin(); // Poistaa kurssinäkymän näkyvistä
                    } else {
                        //TODO: error for failed logout
                        return;
                    }
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        );
        if (apiHandler.isLoggedIn()) {
            switchToLogout();
        } else {
            switchToLogin();
        }
    }

    /**
     * Creates the panel that contains the list of available demos and tasks.
     * @param courselist list of courses with tidecli demos.
     */
    private void createCourseListPane(List<Course> courselist) {
        //Removes all previous courses added, to make refreshing possible. TODO:better solution?
        coursePanel.removeAll();
        courselist.forEach(course -> {
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
            final int fontSize = 26;
            labelPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));

            JLabel label = new JLabel();
            label.setText("Course " + course.getName());
            label.setFont(new Font("Arial", Font.BOLD, fontSize));
            labelPanel.add(label);
            coursePanel.add(labelPanel);

            // Makes own subpanel for every task
            // gbc.gridy asettaa ne paikalleen GridBagLayoutissa
            List<CourseTask> tasks = course.getTasks();
            final int[] j = {0};
            tasks.forEach(courseTask -> {
                JPanel subPanel = createExercise(courseTask);
                subPanel.setBackground(bgColor);
                gbc.gridy = j[0];
                panel.add(subPanel, gbc);
                panel.setBackground(bgColor);
                panel.setOpaque(true);
                j[0]++;
            });

            final int thickness = 4;

            // Tehdään scrollpane johon lätkäistään kaikki tähän mennessä tehty.
            JScrollPane scrollPane = new JBScrollPane(panel);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, thickness));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            coursePanel.add(scrollPane);
        });

    }

    /**
     * Gets the content of the panel.
     * @return main panel
     */
    public JPanel getContent() {
        return panel1;
    }

    /**
     * Creates a panel for the task together with the buttons to download or open it.
     * @param courseTask a CourseTask object for which to create the panel
     * @return the subpanel that contains the tasks name and the two buttons
     */
    private JPanel createExercise(CourseTask courseTask) {
        final int fontsize = 16;
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BorderLayout());
        JLabel labelWeek = new JLabel();
        labelWeek.setText(courseTask.getName());
        labelWeek.setFont(new Font("Arial", Font.BOLD, fontsize));
        subPanel.add(labelWeek, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(bgColor);
        subPanel.setBackground(bgColor);

        JButton dButton = new JButton();
        dButton.setText("Download");
        dButton.setBackground(bgColor);
        dButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(courseTask.getPath());
                ApiHandler api = new ApiHandler();
                try {
                    api.loadExercise(courseTask.getPath());
                } catch (IOException ex) {
                    com.views.ErrorView.displayError("Couldn't load exercise. Check Tide CLI", "Download error");
                    throw new RuntimeException(ex);
                    //Maybe there could be more advanced error reporting
                } catch (InterruptedException ex) {
                    com.views.ErrorView.displayError("Couldn't load exercise. Check Tide CLI", "Download error");
                    throw new RuntimeException(ex);
                }
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
    /**
     * Switches to a state where logging out is possible.
     */
    private void switchToLogout() {
        //tabbedPane.remove(loginPane); // Hide Login tab
        ApiHandler apiHandler = new ApiHandler();
        List<Course> courselist = apiHandler.courses();
        // A panel that contains the courses and tasks is created in its own sub-program.
        createCourseListPane(courselist);
        tabbedPane.addTab("Courses", coursesPane); // Show Logout tab
        loginButton.setEnabled(false);
        panel1.revalidate();
        panel1.repaint();
        tabbedPane.setSelectedComponent(coursesPane);
        //loginButton.setText("Logout");
    }
    /**
     * Switches to a state where logging in is possible.
     */
    private void switchToLogin() {
        tabbedPane.remove(coursesPane); // Hide Courses tab
        tabbedPane.addTab("Login", loginPane); // Show Login tab
        loginButton.setEnabled(true);
        panel1.revalidate();
        panel1.repaint();
        tabbedPane.setSelectedComponent(loginPane);
    }

}

