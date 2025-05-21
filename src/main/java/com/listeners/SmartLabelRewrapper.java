package com.listeners;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * a copy of the smartLabelResizer class, that wraps the text instead of using 3 dots if its too long.
 */
public final class SmartLabelRewrapper {
    private SmartLabelRewrapper() { };
    private static final Map<JLabel, String> STRING_HASH_MAP = new HashMap<>();

    /**
     * Sets up a component listener for each label.
     * @param labels A list of labels, namely the course names.
     * @param toolWindow CourseMainPane.
     */
    public static void setupSmartRewrapForLabels(java.util.List<JLabel> labels, ToolWindow toolWindow) {
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
            if (label.getText().contains("<br/>")) {
                label.setText(label.getText().replaceAll("<br/>", " "));
            }
            STRING_HASH_MAP.put(label, label.getText());

            String fullText = STRING_HASH_MAP.get(label);
            if (fullText == null) {
                continue;
            }

            FontMetrics metrics = label.getFontMetrics(label.getFont());
            int textWidth = metrics.stringWidth(fullText);
            if (textWidth <= availableWidth) {
                label.setText(fullText);
            } else {
                String contracted = rewraptextToFit(fullText, metrics, availableWidth);
                label.setText(contracted);
            }
        }
    }

    /**
     * rewraps the label text by replacing an empty space with a <br> tag if the label is too long.
     * @param text Text that needs resizing.
     * @param metrics Metrics of the font being used.
     * @param maxWidth Width of the toolwindow.
     * @return The new text.
     */
    private static String rewraptextToFit(String text, FontMetrics metrics, int maxWidth) {
        if (!text.contains(" ")) {
            return text;
        }
        StringBuilder sb = new StringBuilder();
        if (!text.contains("<html>") && !text.contains("<br/>")) {
            sb.append("<html>");
        }
        for (char c : text.toCharArray()) {
            sb.append(c);
            if (metrics.stringWidth(sb.toString()) > maxWidth && (!sb.toString().contains("<br/>")
             || (sb.toString().startsWith("<br/>") && !sb.substring("<br/>".length()).contains("<br/>")))) {
                try {
                    sb.replace(sb.lastIndexOf(" "), sb.lastIndexOf(" ") + 1, "<br/>");
                } catch (StringIndexOutOfBoundsException e) {

                }
            }
        }
        if (!text.contains("</html>") && !text.contains("<br/>")) {
            sb.append("</html>");
        }
        if (sb.substring(sb.lastIndexOf("<br/>")).contains(" ")
                && metrics.stringWidth(sb.substring(sb.lastIndexOf("<br/>"))) > maxWidth) {
            sb.replace(sb.lastIndexOf("<br/>"), sb.length(), rewraptextToFit(sb.substring(sb.lastIndexOf("<br/>")), metrics, maxWidth));
        }
        return sb.toString();
    }
}
