package com.api;
import com.actions.ActiveState;
import com.intellij.util.messages.Topic;

public interface ActiveStateListener {
    /**
     * TODO.
     */
    Topic<ActiveStateListener> TOPIC = Topic.create("ActiveStateChanges", ActiveStateListener.class);

    /**
     * TODO.
     * @param state TODO.
     */
    void onStateChanged(ActiveState state);

}


