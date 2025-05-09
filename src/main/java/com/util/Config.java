package com.util;

public final class Config {
    private Config() { };
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
}
