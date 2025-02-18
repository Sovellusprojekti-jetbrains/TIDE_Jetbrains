package com.views;

import com.intellij.openapi.ui.Messages;

public class ErrorView {
    /**
     * Displays error message on screen
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
}
