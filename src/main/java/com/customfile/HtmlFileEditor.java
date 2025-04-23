package com.customfile;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Key;
import com.intellij.ui.jcef.JBCefBrowser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;

public final class HtmlFileEditor implements FileEditor {
    private final JBCefBrowser browser;

    /**
     * a method that loads a html file into the middle file editor window.
     * @param url the url that gets loaded into the middle editor window.
     */
    public HtmlFileEditor(String url) {
        browser = new JBCefBrowser();
        browser.loadURL(url); // Load the website
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return browser.getComponent();
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return browser.getComponent();
    }

    @NotNull
    @Override
    public String getName() {
        return "Website Viewer";
    }

    @Override
    public void dispose() {
        Disposer.dispose(browser);
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) { }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) { }

    @Nullable
    @Override
    public FileEditorState getState(@NotNull FileEditorStateLevel level) {
        return null;
    }

    @Override
    public void setState(@NotNull FileEditorState state) { }

    @Nullable
    @Override
    public FileEditorLocation getCurrentLocation() {
        return null;
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
    public <T> @Nullable T getUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) { }
}
