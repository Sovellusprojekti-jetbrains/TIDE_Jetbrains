package com.actions;

import com.api.ActiveStateListener;
import com.intellij.openapi.application.ApplicationManager;

public class ActiveStateManager implements ActiveStateListener {

    private ActiveState state = new ActiveState();

    /**
     * TODO.
     * @param newState TODO.
     */
    public void updateState(String newState) {
        this.state.setState(newState);

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
        updateState("asdas");
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
        this.updateState("state");
    }

    /**
     * Calls the state manaager for use.
     * @return The state manager.
     */
    public static ActiveStateManager getInstance() {
        return ApplicationManager.getApplication().getService(ActiveStateManager.class);
    }


}


