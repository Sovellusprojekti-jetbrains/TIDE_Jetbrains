package com.listeners;

import com.intellij.openapi.wm.ToolWindow;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that truncates names of courses automatically according to toolwindow size.
 */
public final class SmartLabelResizer {
    private SmartLabelResizer() { };
    private static final Map<JLabel, String> STRING_HASH_MAP = new HashMap<>();

    /**
     * Sets up a component listener for each label.
     * @param labels A list of labels, namely the course names.
     * @param toolWindow CourseMainPane.
     */
    public static void setupSmartResizeForLabels(java.util.List<JLabel> labels, ToolWindow toolWindow) {
        for (JLabel label : labels) {
            STRING_HASH_MAP.put(label, label.getText());
        }

        JComponent component = toolWindow.getComponent();

        component.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateLabels(labels, component.getWidth());
            }
        });
        updateLabels(labels, component.getWidth());
    }

    /**
     * Updates the labels to fit the available space.
     * @param labels List of course names.
     * @param availableWidth Width of the toolwindow.
     */
    private static void updateLabels(java.util.List<JLabel> labels, int availableWidth) {
        for (JLabel label : labels) {
            String fullText = STRING_HASH_MAP.get(label);
            if (fullText == null) {
                continue;
            }

            FontMetrics metrics = label.getFontMetrics(label.getFont());
            int textWidth = metrics.stringWidth(fullText);
            if (textWidth <= availableWidth) {
                label.setText(fullText);
            } else {
                String contracted = contractTextToFit(fullText, metrics, availableWidth);
                label.setText(contracted);
            }
        }
    }

    /**
     * Changes the label text to the correct size, adding ellipses if it's too big otherwise.
     * @param text Text that needs resizing.
     * @param metrics Metrics of the font being used.
     * @param maxWidth Width of the toolwindow.
     * @return The new text.
     */
    private static String contractTextToFit(String text, FontMetrics metrics, int maxWidth) {
        // Remove three spaces worth of width from the toolwindow's width to make sure there's no issues.
        int allowedWidth = maxWidth - metrics.stringWidth("   ");
        String ellipsis = "..."; // double space for a bit of padding, because the scrollbar obscures them otherwise.
        int ellipsisWidth = metrics.stringWidth(ellipsis);

        if (ellipsisWidth > allowedWidth) {
            return ""; // No space even for ellipsis
        }

        int available = allowedWidth - ellipsisWidth;
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append(c);
            if (metrics.stringWidth(sb.toString()) > available) {
                sb.setLength(sb.length() - 1); // remove last char that overflows
                break;
            }
        }
        return sb + ellipsis;
    }
}
