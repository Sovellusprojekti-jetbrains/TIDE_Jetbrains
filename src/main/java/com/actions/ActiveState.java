package com.actions;

public class ActiveState {
    private int count;
    private String state;

    public ActiveState() {

    }

    public int getCount() {
        return count;
    }

    public void setState(String newState) {
        state = newState;
    }

    public void increment() {
        count++;
    }
}
