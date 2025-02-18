package com.views;

import com.intellij.openapi.ui.Messages;

public final class ErrorView {
    /**
     * Displays error message on screen.
     * @param message Error message as String
     * @param title Title for error message
     */
    public static void displayError(String message, String title) {
        Messages.showMessageDialog(
                message,
                title,
                Messages.getInformationIcon()
        );
    }


    /**
     * Hide utility class constructor so it can't be instantiated.
     */
    private ErrorView() {

    }
}
