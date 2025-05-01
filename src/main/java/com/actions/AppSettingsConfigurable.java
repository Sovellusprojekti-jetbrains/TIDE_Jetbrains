package com.actions;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import com.state.StateManager;
import com.views.SettingsScreen;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

/**
 * This class provides controller functionality for application settings.
 */
public final class AppSettingsConfigurable implements Configurable {
    private SettingsScreen mySettingsComponent; //Reference to the SettingsScreen panel

    /**
     * Header for TIDE settings.
     * @return header as String
     */
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "TIDE Settings";
    }

    /**
     * Creates the content for TIDE settings. Uses SettingsScreen so ok and cancel buttons are removed.
     * @return JPanel with content
     */
    @Override
    public @Nullable JComponent createComponent() {
        this.mySettingsComponent = new SettingsScreen();
        JPanel settingsView = this.mySettingsComponent.getContent();
        JPanel settingsWrapper = new JPanel(new BorderLayout());
        settingsWrapper.add(settingsView, BorderLayout.NORTH);
        return settingsWrapper;
    }

    /**
     * Checks if changes are made. Settings in idea provides functionality to revert changes.
     * @return true if changes to settings were made, false otherwise
     */
    @Override
    public boolean isModified() {
        StateManager state =
                Objects.requireNonNull(ApplicationManager.getApplication().getService(StateManager.class));
        if (!this.mySettingsComponent.getPathText().equals(state.getPath())) {
            return true;
        }
        if (this.mySettingsComponent.getScrollSpeedSpinnerValue() != state.getScrollSpeed()) {
            return true;
        }
        if (!this.mySettingsComponent.getTidePath().equals(state.getTidePath())) {
            return true;
        }
        return false;
    }

    /**
     * Checks validity of the user defined settings and applies them if everything is ok.
     * @throws ConfigurationException if validation fails
     */
    @Override
    public void apply() throws ConfigurationException {
        File tempFile = new File(this.mySettingsComponent.getPathText());
        int spinnerValue = this.mySettingsComponent.getScrollSpeedSpinnerValue();
        String tidePathString = this.mySettingsComponent.getTidePath();
        StateManager state =
                Objects.requireNonNull(ApplicationManager.getApplication().getService(StateManager.class));
        if (!tempFile.exists()
                || (spinnerValue < 1 || state.getMaxScrollSpeed() < spinnerValue
                )) {
            this.reset();
            throw new ConfigurationException("Please input valid settings!");
        } else {
            com.actions.Settings.savePath(this.mySettingsComponent.getPathText());
            com.actions.Settings.setScrollSpeed(spinnerValue);
            com.actions.Settings.saveTidePath(tidePathString);

        }

    }

    /**
     * Reverts state back to the one found in StateManager.
     */
    @Override
    public void reset() {
        StateManager state =
                Objects.requireNonNull(ApplicationManager.getApplication().getService(StateManager.class));
        this.mySettingsComponent.setPathText(state.getPath());
        this.mySettingsComponent.setScrollSpeedSpinnerValue(state.getScrollSpeed());
        this.mySettingsComponent.setTidePathText(state.getTidePath());
    }

    /**
     * Disposes SettingScreen.
     */
    @Override
    public void disposeUIResources() {
        this.mySettingsComponent = null;
    }
}
