package com.actions;

import com.api.ActiveStateListener;
import com.intellij.openapi.application.ApplicationManager;

public class ActiveStateManager implements ActiveStateListener {

    private int count = 0;
    private String state = "";

    /**
     * TODO.
     * @param newState TODO.
     */
    public void updateState(String newState) {
        this.state = newState;

        // Notify listeners
        ApplicationManager.getApplication()
                .getMessageBus()
                .syncPublisher(ActiveStateListener.TOPIC)
                .onStateChanged(newState);
    }

    /**
     * TODO.
     * @param newState TODO.
     */
    @Override
    public void onStateChanged(String newState) {
        increment();
    }

    /**
     * TODO.
     * @return TODO.
     */
    public int getCount() {
        return count;
    }

    /**
     * Increments count.
     */
    public void increment() {
        count++;
    }

    /**
     * Calls the state manaager for use.
     * @return The state manager.
     */
    public static ActiveStateManager getInstance() {
        return ApplicationManager.getApplication().getService(ActiveStateManager.class);
    }
}
