package com.views;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;

/**
 * Custom file editor.
 */
public class MyCustomFileEditor implements FileEditor {

    /**
     * Dark grey color.
     */
    private final int[] color = {40, 40, 40};

    /**
     * Pain pane of the window.
     */
    private final JPanel panel;
    /**
     * Background color defined above.
     */
    private Color bgColor = new Color(color[0], color[1], color[2]);

    /**
     * Constructor for the custom file editor. TODO: This is a copy of another file
     * @param file some file? TODO: explain.
     */
    public MyCustomFileEditor(@NotNull final VirtualFile file) {

        panel = new JBPanel<>();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String[] courses = new String[]{"A", "B"};
        for (int i = 0; i < courses.length; i++) {
            JPanel panel1 = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            final int[] content = {20, 5, 0};

            // Kurssin nimelle vähän tilaa yläpuolelle
            JPanel labelPanel = new JPanel(new BorderLayout());
            labelPanel.setBorder(BorderFactory.createEmptyBorder(content[0], content[1], content[1], content[2]));

            final int fontSize = 26;
            JLabel label = new JLabel();
            label.setText("Course " + courses[i]);
            label.setFont(new Font("Arial", Font.BOLD, fontSize));
            labelPanel.add(label);
            panel1.add(labelPanel);

            final int tempNumber = 10;
            // Random-viikkotehtävät
            // gbc.gridy asettaa ne paikalleen GridBagLayoutissa
            for (int j = 0; j < tempNumber; j++) {
                JPanel subPanel = createExercise(j);
                subPanel.setBackground(bgColor);
                gbc.gridy = j;
                panel1.add(subPanel, gbc);
            }
            panel1.setBackground(bgColor);
            panel1.setOpaque(true);

            final int size = 300;
            final int thickness = 4;

            // Tehdään scrollpane johon lätkäistään kaikki tähän mennessä tehty.
            JScrollPane scrollPane = new JBScrollPane(panel1);
            scrollPane.setPreferredSize(new Dimension(size, size)); // Set limited height
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, thickness));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            panel.add(scrollPane);
        }


        /*
        JLabel label = new JLabel("This is a custom view.");
        JBTextField textField = new JBTextField();
        textField.setEditable(false); // Prevents typing issues

        JButton button = new JButton("Click Me");
        button.addActionListener(e -> JOptionPane.showMessageDialog(null, "Button Clicked!"));

        panel.add(label);
        panel.add(textField);
        panel.add(button);

         */
    }

    /**
     * Luo rivin viikkotehtävälle nappeineen.
     * @param name annettu nimi
     * @return Viikkotehtävärivi
     */
    JPanel createExercise(final int name) {
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
     * Getter for a panel.
     * @return panel.
     */
    @NotNull
    @Override
    public JComponent getComponent() {
        return panel;
    }

    /**
     * Getter for a focused component.
     * @return panel.
     */
    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return panel;
    }

    /**
     * Getter for the name.
     * @return name.
     */
    @NotNull
    @Override
    public String getName() {
        return "My Custom Editor";
    }

    /**
     * Cleanup if needed.
     */
    @Override
    public void dispose() {
        // Cleanup if needed
    }

    /**
     * Sets the state.
     * @param state State to be set.
     */
    @Override
    public void setState(@NotNull final FileEditorState state) {

    }

    /**
     * Gets the state of the file editor.
     * @param level ???
     * @return State of the editor.
     */
    @NotNull
    @Override
    public FileEditorState getState(@NotNull final FileEditorStateLevel level) {
        return new FileEditorState() {
            @Override
            public boolean canBeMergedWith(@NotNull final FileEditorState otherState, @NotNull final FileEditorStateLevel level) {
                return false; // No merging needed
            }
        };
    }

    /**
     * Is the window modified?
     * @return answer.
     */
    @Override
    public boolean isModified() {
        return false;
    }

    /**
     * Is the window valid?
     * @return true.
     */
    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * Adds a property change listener.
     * @param listener Listener to be added.
     */
    @Override
    public void addPropertyChangeListener(@NotNull final PropertyChangeListener listener) {

    }

    /**
     * Removes the change listener.
     * @param listener Listener to be removed.
     */
    @Override
    public void removePropertyChangeListener(@NotNull final PropertyChangeListener listener) {

    }

    /**
     * Getter for user data.
     * @param key Key.
     * @return Null. TODO
     * @param <T> TODO
     */
    public <T> @Nullable T getUserData(@NotNull final Key<T> key) {
        return null;
    }

    /**
     * Does nothing.
     * @param key Nothing.
     * @param value Nothing.
     * @param <T> Nothing.
     */
    @Override
    public <T> void putUserData(@NotNull final Key<T> key, @Nullable T value) {
        // No-op
    }
}
