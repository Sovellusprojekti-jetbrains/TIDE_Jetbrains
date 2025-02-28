package com.views;

import com.intellij.openapi.ui.Messages;

public final class InfoView {
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
     * Displays warning message on screen with ok/cancel dialog.
     * @param message Warning message as String
     * @param title Title for warning message
     * @return true if user press cancel, false if user press ok
     */
    public static boolean displayOkCancelWarning(String message, String title) {
        int selection = Messages.showOkCancelDialog(message,
                title,
                "OK",
                "Cancel",
                Messages.getWarningIcon());
        return selection == 2;
    }


    /**
     * Hide utility class constructor so it can't be instantiated.
     */
    private InfoView() {

    }
}
