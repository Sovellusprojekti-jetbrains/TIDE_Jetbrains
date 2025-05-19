package com.util;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public final class Config {
    private Config() {
    }
    /**
     * URL for the user manual.
     */
    public static final String MANUAL_URL = "https://tim.jyu.fi/view/"
            + "kurssit/tie/proj/2025/tide-jetbrains/tide-jetbrains-lisaosan-kayttoohjeet";
    /**
     * Base URL for the browser window.
     */
    public static final String BROWSER_BASE_URL = "https://tim.jyu.fi/view/";
    /**
     * Diretory for the log file.
     */
    public static final String LOG_PATH = "%t/tide-cli_log.txt";
    /**
     * Default scroll speed for course view.
     */
    public static final int DEFAULT_SCROLL_SPEED = 16;
    /**
     * Upper limit for the above setting.
     */
    public static final int MAX_SCROLL_SPEED = 1000;
    /**
     * Will the "Open in TIM"-button open the file in the system default browser?
     */
    public static final boolean OPEN_IN_BROWSER = false;
    /**
     * Border thickness for a single course.
     */
    public static final int COURSE_BORDER = 2;

    /**
     * GridBagConstraints for subpanels in CourseMainPane.createCourseListPane.
     *
     * @return new GridBagConstraints.
     */
    public static GridBagConstraints subtaskConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    /**
     * GridBagConstraints added in singleCourse.add(label, gbc).
     *
     * @return GridBagConstraints.
     */
    public static GridBagConstraints singleCourseConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    /**
     * Border for infoPane in CourseTaskPane.
     */
    public static final Border INFOPANEBORDER = BorderFactory.createEmptyBorder(15, 0, 0, 0);
    /**
     * Border for textPane in CourseTaskPane.
     */
    public static final Border TEXTPANEBORDER = BorderFactory.createEmptyBorder(0, 20, 0, 0);
    /**
     * Border for buttonPanel in CourseTaskPane.
     */
    public static final Border BUTTONPANELBORDER = BorderFactory.createEmptyBorder(0, 20, 0, 0);
}
