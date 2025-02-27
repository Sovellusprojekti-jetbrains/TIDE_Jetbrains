package com.actions;

import com.intellij.openapi.application.ApplicationManager;

import java.beans.PropertyChangeListener;

public class ActiveStateManager {

    private ActiveState state = new ActiveState();


    /**
     * Add listener for a specific property change in the ActiveState.
     */
    public void addStatePropertyChangeListener(PropertyChangeListener listener) {
        state.addPropertyChangeListener(listener);
    }

    /**
     * Remove listener for a specific property change in the ActiveState.
     */
    public void removeStatePropertyChangeListener(PropertyChangeListener listener) {
        state.removePropertyChangeListener(listener);
    }

    /**
     * TODO.
     * @return TODO.
     */
    public int getCount() {
        return state.getCount();
    }

    /**
     * Increments count.
     */
    public void increment() {
        state.increment();
    }

    /**
     * Calls the state manager for use.
     * @return The state manager.
     */
    public static ActiveStateManager getInstance() {
        return ApplicationManager.getApplication().getService(ActiveStateManager.class);
    }

    /**
     * Forwards the course fetch command to the state.
     */
    public void updateCourses() {
        state.updateCourses();
    }


}


