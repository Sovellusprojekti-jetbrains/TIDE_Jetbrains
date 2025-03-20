package com.views;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;

public final class InfoView {
    /**
     * Displays user notification bubble of a certain type.
     * TODO: How to customize bubble's fill color etc?
     * @param message Bubble's text content.
     * @param type Notification's type.
     */
    private static void displayBubble(String message, NotificationType type) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("Custom Notification Group")
                .createNotification(message, type)
                .notify(ProjectManager.getInstance().getOpenProjects()[0]); //This should return the open project
    }

    /**
     * Displays error bubble on screen down right corner.
     * @param message Error message as String
     */
    public static void displayError(String message) {
        displayBubble(message, NotificationType.ERROR);
    }

    /**
     * Displays warning bubble on screen down right corner.
     * @param message Warning message as String.
     */
    public static void displayWarning(String message) {
        displayBubble(message, NotificationType.WARNING);
    }

    /**
     * Displays information bubble on screen down right corner.
     * @param message Information message as String.
     */
    public static void displayInfo(String message) {
        displayBubble(message, NotificationType.INFORMATION);
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
