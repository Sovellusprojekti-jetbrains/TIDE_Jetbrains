package com.api;
import com.intellij.util.messages.Topic;

public interface ActiveStateListener {
    /**
     * TODO.
     */
    Topic<ActiveStateListener> TOPIC = Topic.create("ActiveStateChanges", ActiveStateListener.class);

    /**
     * TODO.
     * @param newState TODO.
     */
    void onStateChanged(String newState);

    /**
     * TODO.
     * @return TODO.
     */
    int getCount();
}
