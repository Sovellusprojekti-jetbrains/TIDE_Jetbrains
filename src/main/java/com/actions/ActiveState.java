package com.actions;

/**
 * Contains all the information the running plugin needs to synchronize.
 */
public class ActiveState {
    private int count;

    /**
     * Constructor for ActiveState.
     */
    public ActiveState() {

    }

    /**
     * Gets the number that doesn't do anything and should be removed.
     * @return An integer.
     */
    public int getCount() {
        return count;
    }

    /**
     * Increments the pointless dummy number.
     */
    public void increment() {
        count++;
    }
}
