package com.views;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.ui.JBFont;
import com.listeners.SmartLabelResizer;
import com.state.ActiveState;
import com.actions.Settings;
import com.api.ApiHandler;
import com.api.LogHandler;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.openapi.project.Project;
import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;

import com.course.*;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.AsyncProcessIcon;
import com.util.Config;

import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.createEmptyBorder;

/**
 * Class for displaying a template course window.
 */
public class CourseMainPane {
    private Project project;
    private JButton loginButton;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel loginPane;
    private JPanel titlePanel; // TODO: Does this exist for a reason?
    private JLabel courseLabel;
    private JPanel coursePanel;
    private JScrollPane coursesPane; // TODO: This is not meant to scroll under any circumstances.
    private JButton logoutButton;
    private JButton settingsButton;
    private JButton refreshButton;
    private JLabel timLabel;
    private JProgressBar progressBar1;
    private JProgressBar coursesProgress;
    private ToolWindow thisToolWindow;
    private List<JLabel> labelList;

    /**
     * Creator for the CourseMainPane class, that holds the courses and tasks.
     * @param toolWindow The Toolwindow this view belongs to
     */
    public CourseMainPane(ToolWindow toolWindow) {
        thisToolWindow = toolWindow;
        this.project = toolWindow.getProject();
        // This setLayout needs to be here, else a nullpointer exception happens.
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));

        coursesPane.getVerticalScrollBar().setUnitIncrement(Settings.getScrollSpeed());
        coursesPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // This overrides the form's own font to use a default JetBrains font.
        courseLabel.setFont(JBFont.h0().asBold());

        addActionListeners();

        switchToLoggedOut();
    }


    private void addActionListeners() {
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActiveState.getInstance().updateCourses();
                setProgress(true, "Updating courses...");
            }
        });
        // Currently assumes that the user has the TIM CLI installed.
        // Need some checks and tests in the future.
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setProgress(true, "Logging in...");
                ApiHandler api = new ApiHandler();
                api.login();
            }
        });

        // Adds an action listener for the settings button.
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionManager manager = ActionManager.getInstance();
                AnAction action = manager.getAction("com.actions.Settings");
                manager.tryToExecute(action, null, null, null, true);
            }
        });

        // Adds an action listener for the logout button.
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setProgress(true, "Logging out...");
                ApiHandler api = new ApiHandler();
                api.logout();
            }
        });
        ActiveState stateManager = ActiveState.getInstance();
        stateManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("logout".equals(evt.getPropertyName())) {
                    LogHandler.logInfo("CourseMainPane received event logout");
                    switchToLoggedOut(); // Hides the course view.
                }
                if ("login".equals(evt.getPropertyName())) {
                    LogHandler.logInfo("CourseMainPane received event login");
                    switchToLoggedIn();
                }
                if ("courseList".equals(evt.getPropertyName())) {
                    LogHandler.logInfo("CourseMainPane received event courseList");
                    // Do some extra processing to ensure the courseList shows up as a list instead of object.
                    Object newValue = evt.getNewValue();

                    if (newValue instanceof List<?> rawList) {
                        // Ensure it's a List<Course>
                        if (!rawList.isEmpty() && rawList.get(0) instanceof Course) {
                            List<Course> courses = (List<Course>) rawList;
                            updateCourseContent(courses);
                        } else {
                            System.err.println("Received list, but not of type Course!");
                        }
                    } else {
                        System.err.println("Unexpected event value type: " + newValue);
                    }
                }
                if ("scrollSpeed".equals(evt.getPropertyName())) {
                    coursesPane.getVerticalScrollBar().setUnitIncrement(Settings.getScrollSpeed());
                }
            }
        });
    }

    /**
     * Creates the panel that contains the list of available demos and tasks.
     *
     * @param courselist list of courses with tidecli demos.
     */
    private void createCourseListPane(List<Course> courselist) {
        ApplicationManager.getApplication().invokeLater(() -> {
            coursePanel.removeAll();
            labelList = new ArrayList<>(); // New label list for resizing purposes.
            for (Course course: courselist) {
                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.weightx = 1.0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                final int top = 20;
                final int left = 0;
                final int bottom = 5;
                final int right = 0;

                JLabel label = new JLabel();
                label.setText(" " + course.getName());
                label.setFont(JBFont.h1().asBold());
                label.setBorder(createEmptyBorder(top, left, bottom, right));
                label.setHorizontalAlignment(SwingConstants.LEFT);
                labelList.add(label);

                JPanel singleCourse = new JPanel(new GridBagLayout());

                createSubPanels(course, panel, gbc);

                final int thickness = Config.COURSE_BORDER;

                JScrollPane scrollPane = new JBScrollPane(panel);
                scrollPane.setBorder(BorderFactory.createLineBorder(JBColor.border(), thickness));
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

                gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.NORTHWEST;
                gbc.weightx = 1.0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                singleCourse.add(label, gbc);

                // Add scrollPane below label, but restrict expansion
                gbc.gridy = 1;
                gbc.weighty = 0; // Prevent vertical stretching
                gbc.fill = GridBagConstraints.HORIZONTAL; // Allow width expansion but not height
                singleCourse.add(scrollPane, gbc);

                coursePanel.add(singleCourse);
                SmartLabelResizer.setupSmartResizeForLabels(labelList, thisToolWindow);
            }
        });
    }

    /**
     * Makes a subpanel for every task. Place defined by gbc.gridy.
     * @param course Course the subtasks get added to.
     * @param panel The Panel they get added to.
     * @param gbc Grid Bag Constraints.
     */
    private void createSubPanels(Course course, JPanel panel, GridBagConstraints gbc) {
        List<CourseTask> tasks = course.getTasks();
        int j = 0;
        for (CourseTask courseTask: tasks) {
            courseTask.setParent(course);
            JPanel subPanel = createExercise(courseTask, course.getName());
            subPanel.setBackground(JBColor.background());
            gbc.gridy = j;
            panel.add(subPanel, gbc);
            panel.setBackground(JBColor.background());
            panel.setOpaque(true);
            j++;
        }
    }

    /**
     * Gets the content of the panel.
     *
     * @return main panel
     */
    public JPanel getContent() {
        return mainPanel;
    }

    /**
     * Creates a panel for the task together with the buttons to download or open it.
     * @param courseTask a CourseTask object for which to create the panel
     * @param courseName Course name for save path
     * @return the subpanel that contains the tasks name and the two buttons
     */
    private JPanel createExercise(CourseTask courseTask, String courseName) {
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BorderLayout());
        JLabel labelWeek = new JLabel();
        labelWeek.setText(courseTask.getName());
        labelWeek.setFont(JBFont.medium().asBold());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(JBColor.background());
        subPanel.setBackground(JBColor.background());

        AsyncProcessIcon spinner = new AsyncProcessIcon("Loading");
        spinner.setVisible(false);  // Initially hidden
        buttonPanel.add(spinner);
        JButton downloadButton = new JButton();
        downloadButton.setText("Download");
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spinner.setVisible(true);
                setProgress(true, "Downloading...");
                System.out.println(courseTask.getPath());
                ApiHandler api = new ApiHandler();
                try {
                    api.loadExercise(courseName, courseTask.getPath(), "--all");
                } catch (IOException ex) {
                    com.api.LogHandler.logError("268 CourseMainPane.createExercise(CourseTask courseTask, String courseName)", ex);
                    com.api.LogHandler.logDebug(new String[]{"268 CourseTask courseTask", "268 String courseName"},
                            new String[]{courseTask.toString(), courseName});
                    InfoView.displayError("Couldn't load exercise. Check Tide CLI");
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    com.api.LogHandler.logError("268 CourseMainPane.createExercise(CourseTask courseTask, String courseName)", ex);
                    InfoView.displayError("Couldn't load exercise. Check Tide CLI");
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(downloadButton);

        JButton openButton = new JButton();
        openButton.setText("Open as Project");
        openButton.addActionListener(event -> {
            new ApiHandler().openTaskProject(Settings.getPath() + File.separatorChar + courseName);
        });
        buttonPanel.add(openButton);

        JPanel nameAndButtonPanel = new JPanel(new BorderLayout());
        nameAndButtonPanel.add(labelWeek, BorderLayout.WEST);
        nameAndButtonPanel.add(buttonPanel, BorderLayout.EAST);
        FontMetrics metrics = labelWeek.getFontMetrics(labelWeek.getFont());
        int borderpad = metrics.stringWidth("    ");
        nameAndButtonPanel.setBorder(createEmptyBorder(0, borderpad, 0, 0));
        subPanel.add(nameAndButtonPanel);

        try {
            createSubTaskpanel(subPanel, courseTask);
        } catch (Exception e) {
            com.api.LogHandler.logError("318 CourseMainPane.createExercise createSubTaskpanel", e);
            throw new RuntimeException(e);
        }
        return subPanel;
    }

    /**
     * Creates and adds a panel of clickable subtasks to the panel containing the course that the subtasks belong to.
     * @param subPanel the panel that the panel of subtasks is appended to.
     * @param courseTask The Course task that the subtasks belong to.
     */
    private void createSubTaskpanel(JPanel subPanel, CourseTask courseTask) {
        Tree tree = createTree(courseTask);
        if (tree.getRowCount() != 0) {
            JBScrollPane container = new JBScrollPane();
            container.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            container.add(tree);
            container.setViewportView(tree);
            subPanel.add(container, BorderLayout.SOUTH);
            container.setBorder(createEmptyBorder());
        }
    }

    /**
     * Creates a clickable Tree view used to show subtasks and their files.
     * @param courseTask The coursetask that the subtasks belong to
     * @return The created tree view.
     */
    private Tree createTree(CourseTask courseTask) {
        ApiHandler api = new ApiHandler();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(courseTask.getName());
        int rowCount = 0;
        var subtasks = courseTask.getSubtasks();
        if (subtasks != null) {
            for (SubTask task : subtasks) {
                DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(task);
                for (SubTask.TaskFile file: task.getTaskFiles()) {
                    DefaultMutableTreeNode submitNode = new DefaultMutableTreeNode(file.getFileName());
                    leaf.add(submitNode);
                    rowCount++;
                }
                root.add(leaf);
                rowCount++;
            }
        }
        Tree tree = new Tree(root);
        tree.setRootVisible(false);
        tree.setVisibleRowCount(rowCount);
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }

        tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                updateTree();
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                updateTree();
            }

            /**
             * Updates tree.
             */
            private void updateTree() {
                SwingUtilities.invokeLater(() -> {
                    tree.setVisibleRowCount(tree.getRowCount());
                    refreshPanel(mainPanel);
                });
            }
        });

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
                        SubTask taskToOpen = (SubTask) parent.getUserObject();
                        String taskPath;
                        if (taskToOpen.getTaskDirectory() == null) {
                            taskPath = Settings.getPath() + File.separatorChar + courseTask.getParent().getName()
                                    + File.separatorChar + selectedNode.getRoot()
                                    + File.separatorChar + parent + File.separatorChar + selectedNode;
                        } else {
                            taskPath = Settings.getPath() + File.separatorChar + courseTask.getParent().getName() + File.separatorChar
                                    + taskToOpen.getTaskDirectory()
                                    + File.separatorChar + selectedNode.toString().replace('/', File.separatorChar);
                        }
                        api.openTaskProject(taskPath);
                    }
                }
            }
        });
        tree.setCellRenderer(new SubmitRenderer());
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.collapseRow(i);
        }
        return tree;
    }


    /**
     * Switches to a state where logging out is possible.
     */
    private void switchToLoggedIn() {
        ActiveState stateManager = ActiveState.getInstance();
        stateManager.updateCourses();
        setProgress(true, "Loading courses...");
        // A panel that contains the courses and tasks is created in its own sub-program.
        tabbedPane.addTab("Courses", coursesPane); // Show Logout tab
        loginButton.setText("Logout");
        ActionListener[] tempLogin = loginButton.getActionListeners();
        loginButton.removeActionListener(tempLogin[0]);
        ActionListener[] tempLogout = logoutButton.getActionListeners();
        loginButton.addActionListener(tempLogout[0]);
        refreshPanel(mainPanel);
        tabbedPane.setSelectedComponent(coursesPane);
    }


    /**
     * Switches to a state where logging in is possible.
     */
    private void switchToLoggedOut() {
        tabbedPane.remove(coursesPane); // Hide Courses tab
        tabbedPane.addTab("Menu", loginPane); // Show Login tab
        loginButton.setText("Login");
        ActionListener[] tempLogin = loginButton.getActionListeners(); //Need to change LoginButton back
        loginButton.removeActionListener(tempLogin[0]);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setProgress(true, "Logging in...");
                ApiHandler api = new ApiHandler();
                api.login();
            }
        });
        refreshPanel(mainPanel);
        tabbedPane.setSelectedComponent(loginPane);
        setProgress(false, "");
    }

    /**
     * Updates the UI with the course list.
     * @param courselist Courselist to update with.
     */
    private void updateCourseContent(List<Course> courselist) {
        ApplicationManager.getApplication().invokeLater(() -> {
            createCourseListPane(courselist);
            refreshPanel(mainPanel);
            setProgress(false, "");
        });
    }

    /**
     * Sets the progress bars on both tabs of the course panel to the desired visibility and text.
     * @param state Visible, true or false.
     * @param text Text to display on progress bar.
     */
    private void setProgress(boolean state, String text) {
        ApplicationManager.getApplication().invokeLater(() -> {
            progressBar1.setString(text);
            progressBar1.setVisible(state);
            coursesProgress.setString(text);
            coursesProgress.setVisible(state);
            refreshPanel(mainPanel);
        });
    }

    /**
     * Revalidates and repaints the panel.
     * @param panel The panel to be refreshed.
     */
    private void refreshPanel(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }
}
