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

public class MyCustomFileEditor implements FileEditor {
    private final JPanel panel;
    Color bgColor = new Color(40,40,40);
    public MyCustomFileEditor(@NotNull VirtualFile file) {

        panel = new CourseView().getContent();
    }










    @NotNull
    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return panel;
    }

    @NotNull
    @Override
    public String getName() {
        return "My Custom Editor";
    }

    @Override
    public void dispose() {
        // Cleanup if needed
    }

    @Override
    public void setState(@NotNull FileEditorState state) {}

    @NotNull
    @Override
    public FileEditorState getState(@NotNull FileEditorStateLevel level) {
        return new FileEditorState() {
            @Override
            public boolean canBeMergedWith(@NotNull FileEditorState otherState, @NotNull FileEditorStateLevel level) {
                return false; // No merging needed
            }
        };
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {}

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {}


    public <T> @Nullable T getUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {
        // No-op
    }
}
