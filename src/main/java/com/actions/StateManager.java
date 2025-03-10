package com.actions;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.*;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements persistent state component and provides a service for getting and setting user defined plugin settings.
 */
@Service
@State(
        name = "StateManager",
        storages = @Storage("MySettings.xml")
)
public final class StateManager implements PersistentStateComponent<StateManager.State> {

    /**
     * Separate state class to hold the user defined settings.
     */
    public static class State {
        /**
         * the path the user has chosen in string format. TODO:check that this is true
         */
        private String path;
    }

    private State myState = new State(); //Object reference to state class

    /**
     * This method is called when updating state class fields and to save the state of the State class when IDE is closed.
     * @return Object reference to State class
     */
    @Override
    public State getState() {
        return myState;
    }

    /**
     * This method is called when IDE is opened to load previous component state.
     * @param state loaded component state
     */
    @Override
    public void loadState(@NotNull State state) {
        myState = state;
    }

    /**
     * Sets new value to path field in State class.
     * @param path File path as a String
     */
    public void setPath(String path) {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        properties.setValue("myPlugin.path", path);
        //String value = properties.getValue("myPlugin.path", System.getProperty("user.dir"));
        //getState().path = path;

    }

    /**
     * Gets the path fields value from State class.
     * @return File ath as a String
     */
    public String getPath() {
        /*
        if (getState().path == null) {
            return System.getProperty("user.dir");
        }
        */
        com.api.LogHandler.logToFile("debug", "getPath() called");
        PropertiesComponent properties = PropertiesComponent.getInstance();
        String value = properties.getValue("myPlugin.path", System.getProperty("user.dir"));
        com.api.LogHandler.logToFile("debug", ("String value = " + value));
        com.api.LogHandler.logToFile("debug", ("return: " + value));
        //System.out.println(getState().path);
        return value;
    }
}
