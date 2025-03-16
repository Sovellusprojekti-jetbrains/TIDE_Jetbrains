package com.views;
import com.actions.ActiveState;
import com.actions.Settings;
import com.api.ApiHandler;
import com.api.JsonHandler;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.*;

import com.course.*;
import com.intellij.ui.treeStructure.Tree;
import java.util.ArrayList;
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
     * TitlePanel seems to exist for a test.
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
    private JLabel timLabel;

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
    private final Color bgColor = JBColor.background();

    /**
     * the scrollspeed for the jscrollpanels.
     */
    private final int scrollSpeed = 16;

    /**
     * Creator for the CustomScreen class, that holds the courses and tasks.
     */
    public CustomScreen() {
        // ilman setLayout-kutsua tämä kaatuu nullpointteriin
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
        ApiHandler apiHandler = new ApiHandler();

        coursesPane.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
        coursesPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

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
                ActiveState.getInstance().updateCourses();
                List<Course> refreshed = ActiveState.getInstance().getCourses();
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
                        ActiveState stateManager = ActiveState.getInstance();
                        stateManager.login();
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
                Project project = ProjectManager.getInstance().getDefaultProject();
                ShowSettingsUtil.getInstance().showSettingsDialog(project, "TIDE settings");


                //Settings temp = new Settings();
                //temp.displaySettings();
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
                        ActiveState stateManager = ActiveState.getInstance();
                        stateManager.logout();
                    } else {
                        //TODO: error for failed logout
                        return;
                    }
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        if (apiHandler.isLoggedIn()) {
            switchToLogout();
            ActiveState stateManager = ActiveState.getInstance();
            stateManager.login();
        } else {
            switchToLogin();
            ActiveState stateManager = ActiveState.getInstance();
            stateManager.logout();
        }
    }

    /**
     * Creates the panel that contains the list of available demos and tasks.
     *
     * @param courselist list of courses with tidecli demos.
     */
    private void createCourseListPane(List<Course> courselist) {
        //Removes all previous courses added, to make refreshing possible. TODO:better solution?
        coursePanel.removeAll();
        for (Course course: courselist) {
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
            label.setText(course.getName());
            label.setFont(new Font("Arial", Font.BOLD, fontSize));
            labelPanel.add(label);
            coursePanel.add(labelPanel);

            // Makes own subpanel for every task
            // gbc.gridy asettaa ne paikalleen GridBagLayoutissa
            List<CourseTask> tasks = course.getTasks();
            final int[] j = {0};
            for (CourseTask courseTask: tasks) {
                courseTask.setParent(course);
                JPanel subPanel = createExercise(courseTask, course.getName());
                subPanel.setBackground(bgColor);
                gbc.gridy = j[0];
                panel.add(subPanel, gbc);
                panel.setBackground(bgColor);
                panel.setOpaque(true);
                j[0]++;
            }

            final int thickness = 4;

            // Tehdään scrollpane johon lätkäistään kaikki tähän mennessä tehty.
            JScrollPane scrollPane = new JBScrollPane(panel);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, thickness));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            coursePanel.add(scrollPane);
        }

    }

    /**
     * Gets the content of the panel.
     *
     * @return main panel
     */
    public JPanel getContent() {
        return panel1;
    }

    /**
     * Creates a panel for the task together with the buttons to download or open it.
     *
     * @param courseTask a CourseTask object for which to create the panel
     * @param courseName Course name for save path
     * @return the subpanel that contains the tasks name and the two buttons
     */
    private JPanel createExercise(CourseTask courseTask, String courseName) {
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
                    api.loadExercise(courseName, courseTask.getPath(), "--all");
                } catch (IOException ex) {
                    InfoView.displayError("Couldn't load exercise. Check Tide CLI", "Download error");
                    throw new RuntimeException(ex);
                    //Maybe there could be more advanced error reporting
                } catch (InterruptedException ex) {
                    InfoView.displayError("Couldn't load exercise. Check Tide CLI", "Download error");
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(dButton);

        JButton oButton = new JButton();
        oButton.setText("Open as Project");
        oButton.setBackground(bgColor);
        oButton.addActionListener(event -> {
            int lastPartStart = courseTask.getPath().lastIndexOf('/');
            String demoDirectory = File.separatorChar + courseTask.getPath().substring(lastPartStart + 1);
            new ApiHandler().openTaskProject(Settings.getPath() + File.separatorChar + courseName + demoDirectory);
        });
        buttonPanel.add(oButton);

        subPanel.add(buttonPanel, BorderLayout.EAST);
        try {
            createSubTaskpanel(subPanel, courseTask, courseName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return subPanel;
    }

    /**
     * Creates and adds a panel of clickable subtasks to the panel containing the course that the subtasks belong to.
     * @param subPanel the panel that the panel of subtasks is appended to.
     * @param courseTask The Course task that the subtasks belong to.
     * @param courseName Course name for subdirectory
     */
    private void createSubTaskpanel(JPanel subPanel, CourseTask courseTask, String courseName) {
        // TODO: Need to implement actual character replacement for illegal filenames.
        // This is a bandage to prevent exceptions when showing illegal test data on screen.
        courseName = courseName.replaceAll("[\\\\/:\"?*|<>]", "_");

        String pathToFile = Settings.getPath() + File.separatorChar + courseName;
        JsonHandler jsonHandler = new JsonHandler();
        String timData = readTimData(pathToFile);
        if (!timData.isEmpty()) {
            List<SubTask> subtasks = jsonHandler.jsonToSubtask(timData);
            Tree tree = createTree(subtasks, courseTask);
            JBScrollPane container = new JBScrollPane();
            container.add(tree);
            container.setViewportView(tree);
            subPanel.add(container);
        }
    }

    /**
     * Creates a clickable Tree view used to show subtasks and their files.
     * @param subtasks Subtask for a course that also contain the files for the subtask.
     * @param courseTask The coursetask that the subtasks belong to
     * @return The created tree view.
     */
    private Tree createTree(List<SubTask> subtasks, CourseTask courseTask) {
        ApiHandler api = new ApiHandler();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(courseTask.getName());
        int rowCount = 0;
        for (SubTask task: subtasks) {
            List<SubTask> listForCourse = new ArrayList<>();
            if (task.getPath().equals(courseTask.getPath())) {
                listForCourse.add(task);
                DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(task.getIdeTaskId());
                for (String file : task.getFileName()) {
                            DefaultMutableTreeNode submitNode = new DefaultMutableTreeNode(file.replaceAll("\"", ""));
                            leaf.add(submitNode);
                            rowCount = rowCount + 1;
                }
                root.add(leaf);
            }
            rowCount = rowCount + listForCourse.size();
            courseTask.setTasks(listForCourse);
        }
        Tree tree = new Tree(root);
        tree.setRootVisible(false);
        tree.setVisibleRowCount(rowCount);
        //TODO: korjaa avaaminen tuplaklikillä lisäämällä  kurssifolderi.
        tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                            tree.getLastSelectedPathComponent();
                    if (node == null) {
                        return;
                    }
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
                    if (selectedNode.getChildCount() == 0) {
                        api.openTaskProject(Settings.getPath() + File.separatorChar + selectedNode.getRoot()
                                + File.separatorChar + parent.toString() + File.separatorChar + selectedNode);
                    } else {
                        api.openTaskProject(Settings.getPath() + File.separatorChar + parent.toString()
                                + File.separatorChar + selectedNode);
                    }
                }
            }
        });
        tree.setCellRenderer(new SubmitRenderer());
        return tree;
    }

    /**
     * Reads the timdata file that is located in the path.
     * @param pathToFile Path in the settings where the timdata file is located after downloading a task.
     * @return timdata in string format, empy if file was not found
     */
    private String readTimData(String pathToFile) {
        StringBuilder sb = new StringBuilder();
        try {
            String settingsPath = pathToFile + File.separatorChar + ".timdata";
            Path path = Paths.get(settingsPath);
            BufferedReader reader = Files.newBufferedReader(path);
            String line = reader.readLine();
            while (line != null) {
                // read next line
                sb.append(line).append(System.lineSeparator());
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("File timdata was not found");
        }
        return sb.toString();
    }

    /**
     * Switches to a state where logging out is possible.
     */
    private void switchToLogout() {
        //tabbedPane.remove(loginPane); // Hide Login tab
        ActiveState stateManager = ActiveState.getInstance();
        stateManager.updateCourses();
        List<Course> courselist = stateManager.getCourses();
        // A panel that contains the courses and tasks is created in its own sub-program.
        createCourseListPane(courselist);
        tabbedPane.addTab("Courses", coursesPane); // Show Logout tab
        loginButton.setText("Logout");
        ActionListener[] tempLogin = loginButton.getActionListeners(); //Need to change LoginButton into LogoutButton
        loginButton.removeActionListener(tempLogin[0]);
        ActionListener[] tempLogout = logoutButton.getActionListeners();
        loginButton.addActionListener(tempLogout[0]);
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
        tabbedPane.addTab("Menu", loginPane); // Show Login tab
        loginButton.setText("Login");
        ActionListener[] tempLogin = loginButton.getActionListeners(); //Need to change LoginButton back
        loginButton.removeActionListener(tempLogin[0]);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApiHandler api = new ApiHandler();
                try {
                    api.login();
                    if (api.isLoggedIn()) {
                        switchToLogout();
                        ActiveState stateManager = ActiveState.getInstance();
                        stateManager.login();
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
        panel1.revalidate();
        panel1.repaint();
        tabbedPane.setSelectedComponent(loginPane);
    }


}
