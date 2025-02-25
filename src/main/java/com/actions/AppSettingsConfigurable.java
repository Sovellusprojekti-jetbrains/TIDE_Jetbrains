package com.actions;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import com.views.SettingsScreen;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public final class AppSettingsConfigurable implements Configurable {

    private SettingsScreen mySettingsComponent;

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "TIDE Settings";
    }

    @Override
    public @Nullable String getDisplayNameFast() {
        return Configurable.super.getDisplayNameFast();
    }

    @Override
    public @Nullable @NonNls String getHelpTopic() {
        return Configurable.super.getHelpTopic();
    }

    @Override
    public void focusOn(@NotNull @Nls String label) {
        Configurable.super.focusOn(label);
    }

    @Override
    public @Nullable JComponent createComponent() {
        this.mySettingsComponent = new SettingsScreen();
        return this.mySettingsComponent.getContent();
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() { //TODO: Tarvitaanko?
        return Configurable.super.getPreferredFocusedComponent();
    }

    @Override
    public boolean isModified() { //TODO: Tarvitaanko?
        return false;
    }

    @Override
    public void apply() throws ConfigurationException { //TODO: Tarvitaanko?

    }

    @Override
    public void reset() { //TODO: Tarvitaanko?
        Configurable.super.reset();
    }

    @Override
    public void disposeUIResources() {
        //Configurable.super.disposeUIResources();
        this.mySettingsComponent = null;
    }

    @Override
    public void cancel() { //TODO: Tarvitaanko?
        Configurable.super.cancel();
    }
}
