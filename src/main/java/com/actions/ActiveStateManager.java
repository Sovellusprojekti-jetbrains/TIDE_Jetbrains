package com.actions;

import com.api.ActiveStateListener;
import com.intellij.openapi.application.ApplicationManager;

public class ActiveStateManager implements ActiveStateListener {

    private ActiveState state = new ActiveState();

    /**
     * TODO.
     */
    public void updateState() {

        // Notify listeners
        ApplicationManager.getApplication()
                .getMessageBus()
                .syncPublisher(ActiveStateListener.TOPIC)
                .onStateChanged(state);
    }

    /**
     * TODO.
     * @param newState TODO.
     */
    @Override
    public void onStateChanged(ActiveState newState) {
        updateState();
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
        this.updateState();
    }

    /**
     * Calls the state manaager for use.
     * @return The state manager.
     */
    public static ActiveStateManager getInstance() {
        return ApplicationManager.getApplication().getService(ActiveStateManager.class);
    }


}


